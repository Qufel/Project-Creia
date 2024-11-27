package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.physics.collisions.CollisionData;
import engine.physics.collisions.IntersectionDetector;
import engine.physics.forces.ForceGenerator;
import engine.physics.forces.ForceRegistration;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CollisionSystem {

    private Engine engine;

    private ArrayList<Collider> colliders;
    private ArrayList<Pair> previousPairs = new ArrayList<Pair>();
    private ArrayList<Pair> possiblePairs = new ArrayList<Pair>();

    //region Getters & Setters

    public void addCollider(Collider collider) {
        colliders.add(collider);
    }

    public void clearColliders() {
        colliders.clear();
    }

    //endregion

    public CollisionSystem(Engine engine) {
        this.engine = engine;
        colliders = new ArrayList<>();
    }

    public void update() {

        getPossiblePairs();

        for (Pair pair : possiblePairs) {

            Collider a = pair.a;
            Collider b = pair.b;

            a.getCollidingObjects().remove(b);
            b.getCollidingObjects().remove(a);

            CollisionData data = new CollisionData();

            PhysicsBody aBody = (PhysicsBody) a.getParent();
            PhysicsBody bBody = (PhysicsBody) b.getParent();

            if (IntersectionDetector.aabbInAABB(a.getAABB(), b.getAABB(), data)) {

                int penetration = (int) Math.abs(data.normal.dot(data.penetration));

                if (!a.getCollidingObjects().contains(b)) {
                    a.getCollidingObjects().add(b);
                }

                if (!b.getCollidingObjects().contains(a)) {
                    b.getCollidingObjects().add(a);
                }

               if (aBody.getMass() != 0 && bBody.isColliding()) {

                   Vector2 aVelocity = aBody.getVelocity();

                   int aVDot = (int) aVelocity.dot(data.normal);

                   if (aVDot < 0 && (data.normal.equals(Vector2.UP) || data.normal.equals(Vector2.DOWN))) {
                       aVelocity.y = 0;
                   }

                   if (aVDot < 0 && (data.normal.equals(Vector2.LEFT) || data.normal.equals(Vector2.RIGHT))) {
                       aVelocity.x = 0;
                   }

                   aBody.setVelocity(aVelocity);
                   aBody.setPosition(aBody.getPosition().add(data.normal.mul(penetration + 1)));

               }

                if (bBody.getMass() != 0 && aBody.isColliding()) {

                    Vector2 bVelocity = bBody.getVelocity();

                    int bVDot = (int) bVelocity.dot(data.normal.mul(-1));

                    if (bVDot < 0 && (data.normal.equals(Vector2.UP) || data.normal.equals(Vector2.DOWN))) {
                        bVelocity.y = 0;
                    }

                    if (bVDot < 0 && (data.normal.equals(Vector2.LEFT) || data.normal.equals(Vector2.RIGHT))) {
                        bVelocity.x = 0;
                    }

                    bBody.setVelocity(bVelocity);
                    bBody.setPosition(bBody.getPosition().add(data.normal.mul(penetration + 1).mul(-1)));

                }


                Vector2 vAB = aBody.getVelocity().sub(bBody.getVelocity());
                double vN = vAB.dot(data.normal);

                float epsilon = 0.2f;

                float impulseForce = (float) ((float) (-(1 + epsilon) * vN) / (data.normal.dot(data.normal) * (aBody.getInverseMass()) + bBody.getInverseMass()));

                Vector2 impulseVector = data.normal.mul(impulseForce);

            }

            previousPairs.addAll(possiblePairs);
        }

    }

    private ArrayList<Pair> getPossiblePairs() {
        possiblePairs = new ArrayList<>();

        for (Collider a : colliders) {
            for (Collider b : colliders) {

                if (a.equals(b)) continue;

                PhysicsBody aBody = (PhysicsBody) a.getParent();
                PhysicsBody bBody = (PhysicsBody) b.getParent();

                Pair pair = new Pair(a, b);
                if (previousPairs.contains(pair)) {
                    possiblePairs.add(previousPairs.get(previousPairs.indexOf(pair)));
                }

                if (aBody.equals(bBody)) continue;
                if (aBody.getVelocity().equals(Vector2.ZERO) && bBody.getVelocity().equals(Vector2.ZERO)) continue;

                if (possiblePairs.contains(pair)) continue;

                possiblePairs.add(pair);

            }
        }

        return possiblePairs;
    }

    public void sortByMinX() {
        int i = 1;
        while (i < this.colliders.size()) {
            int j = i;
            while (j > 0) {
                int aX = this.colliders.get(j - 1).getAABB().getMin().x;
                int bX = this.colliders.get(j).getAABB().getMin().x;

                if (aX > bX) {
                    Collider tmp = this.colliders.get(j - 1);
                    this.colliders.set(j - 1, this.colliders.get(j));
                    this.colliders.set(j, tmp);

                    j = j - 1;
                } else {
                    break;
                }
            }

            i = i + 1;
        }
    }



}

class Pair {
    public Collider a;
    public Collider b;

    public Pair(Collider a, Collider b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Pair pair)) return false;

        return (pair.a == this.a && pair.b == this.b) || (pair.a == this.b && pair.b == this.a);
    }
}