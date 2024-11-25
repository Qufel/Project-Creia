package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.physics.collisions.CollisionData;
import engine.physics.collisions.IntersectionDetector;
import engine.physics.forces.ForceGenerator;
import engine.physics.forces.ForceRegistration;

import java.util.ArrayList;

public class CollisionSystem {

    private Engine engine;

    private ArrayList<Collider> colliders;
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

                System.out.println(aBody.toString() + " is colliding with " + bBody.toString());

//                if (!a.getCollidingObjects().contains(b)) {
//                    a.getCollidingObjects().add(b);
//                }
//
//                if (!b.getCollidingObjects().contains(a)) {
//                    b.getCollidingObjects().add(a);
//                }
//
//                Vector2 vAB = aBody.getVelocity().sub(bBody.getVelocity());
//                double vN = vAB.dot(data.normal);
//
//                float epsilon = 0.2f;
//
//                float impulseForce = (float) ((float) (-(1 + epsilon) * vN) / (data.normal.dot(data.normal) * (aBody.getInverseMass()) + bBody.getInverseMass()));
//
//                Vector2 impulseVector = data.normal.mul(impulseForce);
//
//                Vector2 aVelocity = aBody.getVelocity();
//                Vector2 bVelocity = bBody.getVelocity();
//
//                Vector2 r1 = a.getAABB().getCenter().sub(data.point);
//                Vector2 r2 = b.getAABB().getCenter().sub(data.point);
//
//                double constraint = b.getAABB().getCenter().add(r2).sub(a.getAABB().getCenter()).sub(r1).dot(data.normal);
//                double constraintDiff = bVelocity.sub(aVelocity).dot(data.normal);
//
                System.out.println(data.normal);

//                aBody.setPosition(aBody.getPosition().sub(data.normal.mul(data.penetration).mul(aBody.getInverseMass())));
//                bBody.setPosition(bBody.getPosition().add(data.normal.mul(data.penetration).mul(bBody.getInverseMass())));
//
//                engine.getPhysics().getForceRegistry().add(aBody, new ForceGenerator() {
//                    @Override
//                    public void updateForce(PhysicsBody body, float delta) {
//                        body.addForce(impulseVector.mul(delta * 10).mul(body.getMass()));
//                    }
//                });
//
//                engine.getPhysics().getForceRegistry().add(bBody, new ForceGenerator() {
//                    @Override
//                    public void updateForce(PhysicsBody body, float delta) {
//                        body.addForce(impulseVector.mul(delta * 10).mul(body.getMass()).mul(-1));
//                    }
//                });

//                aBody.addForce(impulseVector.mul(aBody.getMass()));
//                bBody.addForce(impulseVector.mul(aBody.getMass()).mul(-1));

//                aBody.setVelocity(aVelocity.add(impulseVector.mul(aBody.getInverseMass())));
//                bBody.setVelocity(bVelocity.sub(impulseVector.mul(bBody.getInverseMass())));

            } else {
                aBody.onGround(false);
                bBody.onGround(false);
            }
        }

    }

    private ArrayList<Pair> getPossiblePairs() {
        possiblePairs = new ArrayList<>();

        for (Collider a : colliders) {
            for (Collider b : colliders) {

                if (a.equals(b)) continue;

                PhysicsBody aBody = (PhysicsBody) a.getParent();
                PhysicsBody bBody = (PhysicsBody) b.getParent();

                if (aBody.equals(bBody)) continue;
//                if (aBody.getVelocity().equals(Vector2.ZERO) && bBody.getVelocity().equals(Vector2.ZERO)) continue;

                Pair pair = new Pair(a, b);

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