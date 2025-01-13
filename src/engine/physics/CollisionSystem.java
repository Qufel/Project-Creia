package engine.physics;

import engine.EMath;
import engine.Engine;
import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.physics.collisions.CollisionData;
import engine.physics.collisions.IntersectionDetector;
import engine.physics.forces.ForceGenerator;

import java.util.ArrayList;
import java.util.Comparator;

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

            if (pair.a.getAABB().getMin().x > pair.a.getAABB().getMax().x) continue;

            Collider a = pair.a;
            Collider b = pair.b;

            a.getCollidingObjects().remove(b);
            b.getCollidingObjects().remove(a);

            CollisionData data = new CollisionData();

            if (IntersectionDetector.aabbInAABB(a.getAABB(), b.getAABB(), data)) {

                if (!a.getCollidingObjects().contains(b)) {
                    a.getCollidingObjects().add(b);
                }

                if (!b.getCollidingObjects().contains(a)) {
                    b.getCollidingObjects().add(a);
                }

                resolveCollision((PhysicsBody) a.getParent(), (PhysicsBody) b.getParent(), data);
//                oldResolveCollision(a, b, data); // TODO: Temporary
            }

        }

        System.gc();
    }

    private void oldResolveCollision(Collider a, Collider b, CollisionData data) {

        PhysicsBody aBody = (PhysicsBody) a.getParent();
        PhysicsBody bBody = (PhysicsBody) b.getParent();

        int penetration = (int) Math.abs(data.normal.dot(data.penetration));

        if (!a.getCollidingObjects().contains(b)) {
            a.getCollidingObjects().add(b);
        }

        if (!b.getCollidingObjects().contains(a)) {
            b.getCollidingObjects().add(a);
        }

        if (aBody.getMass() != 0 && bBody.isColliding()) {

            aBody.setPosition(aBody.getPosition().add(data.normal.mul(penetration + 1)));

            Vector2 aVelocity = aBody.getVelocity();

            int aVDot = (int) aVelocity.dot(data.normal);

            if (aVDot < 0 && (data.normal.equals(Vector2.UP) || data.normal.equals(Vector2.DOWN))) {
                aVelocity.y = 0;
            }

            if (aVDot < 0 && (data.normal.equals(Vector2.LEFT) || data.normal.equals(Vector2.RIGHT))) {
                aVelocity.x = 0;
            }

            int acc = EMath.ceilFloor(data.normal.dot(aBody.getAcceleration()));

            if (data.normal == Vector2.UP || data.normal == Vector2.RIGHT)
                acc = Math.clamp(acc, 0, Integer.MAX_VALUE);
            else if (data.normal == Vector2.DOWN || data.normal == Vector2.LEFT)
                acc = Math.clamp(acc, Integer.MIN_VALUE, 0);

            aBody.setAcceleration(aBody.getAcceleration().mul(acc));
            aBody.setVelocity(aVelocity);

        }

        if (bBody.getMass() != 0 && aBody.isColliding()) {

            bBody.setPosition(bBody.getPosition().add(data.normal.mul(penetration + 1).mul(-1)));

            Vector2 bVelocity = bBody.getVelocity();

            int bVDot = (int) bVelocity.dot(data.normal.mul(-1));

            if (bVDot < 0 && (data.normal.equals(Vector2.UP) || data.normal.equals(Vector2.DOWN))) {
                bVelocity.y = 0;
            }

            if (bVDot < 0 && (data.normal.equals(Vector2.LEFT) || data.normal.equals(Vector2.RIGHT))) {
                bVelocity.x = 0;
            }

            int acc = EMath.ceilFloor(data.normal.dot(bBody.getAcceleration()));

            if (data.normal == Vector2.UP || data.normal == Vector2.RIGHT)
                acc = Math.clamp(acc, 0, Integer.MAX_VALUE);
            else if (data.normal == Vector2.DOWN || data.normal == Vector2.LEFT)
                acc = Math.clamp(acc, Integer.MIN_VALUE, 0);

            bBody.setAcceleration(bBody.getAcceleration().mul(acc));
            bBody.setVelocity(bVelocity);

        }
    }

    // Resolves collision between A and B using impulse method
    private void resolveCollision(PhysicsBody a, PhysicsBody b, CollisionData data) {
        Vector2 relativeVelocity = a.getVelocity().sub(b.getVelocity());
        float relativeVelocityAlongNormal = relativeVelocity.dot(data.normal);

        if (relativeVelocityAlongNormal > 0) {
            return; // Objects are separating, no impulse required
        }

        // Get elasticity of the collision
        float elasticity = Math.min(a.getRestitution(), b.getRestitution());

        // Calculate an impulse magnitude
        float impulseMagnitude = -(1 + elasticity) * relativeVelocityAlongNormal;
        impulseMagnitude /= a.getInverseMass() + b.getInverseMass();

        // Apply impulse directly to object's velocity
        Vector2 impulseVector = data.normal.mul(impulseMagnitude);
        if (a.getMass() > 0) {
            a.setVelocity(a.getVelocity().add(impulseVector.mul(a.getInverseMass())));
        }
        if (b.getMass() > 0) {
            b.setVelocity(b.getVelocity().sub(impulseVector.mul(b.getInverseMass())));
        }

        // Apply position correction to objects
        Vector2 correction = data.normal.mul(data.penetration.dot(data.normal) / (a.getInverseMass() + b.getInverseMass()));
        if (a.getMass() > 0) {
            a.setPosition(a.getPosition().add(correction.mul(a.getInverseMass())));
        }
        if (b.getMass() > 0) {
            b.setPosition(b.getPosition().sub(correction.mul(b.getInverseMass())));
        }

    }

    private ArrayList<Pair> getPossiblePairs() {
        possiblePairs = new ArrayList<>();

        // TODO: Collision is checked even though objects are not close or are far away from each other.

        for (Collider a : colliders) {
            if (a == null) continue;

            for (Collider b : colliders) {

                if (b == null) continue;
                if (a.equals(b)) continue;

                PhysicsBody aBody = (PhysicsBody) a.getParent();
                PhysicsBody bBody = (PhysicsBody) b.getParent();

                Pair pair = new Pair(a, b);

                if (aBody.equals(bBody)) continue;
                if (aBody.getVelocity().equals(Vector2.ZERO) && bBody.getVelocity().equals(Vector2.ZERO)) continue;

                if (possiblePairs.contains(pair)) continue;

                possiblePairs.add(pair);

            }
        }

        return possiblePairs;
    }

    public void sortByMinX() {
        colliders.sort(Comparator.comparingInt(collider -> collider.getGlobalPosition().x));
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

    @Override
    public String toString() {
        return "( " + a.toString() + ", " + b.toString() + " )";
    }
}