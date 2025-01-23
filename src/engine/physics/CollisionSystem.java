package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.physics.collisions.*;

import java.util.ArrayList;

public class CollisionSystem {

    private Engine engine;

    private ArrayList<Collider> colliders;
    private ArrayList<Pair> possiblePairs = new ArrayList<Pair>();

    private ArrayList<Pair> collidingPairs = new ArrayList<Pair>();
    private ArrayList<Pair> lastCollidingPairs = new ArrayList<Pair>();

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

        collidingPairs.clear();

        for (Pair pair : possiblePairs) {

            Collider a = pair.a;
            Collider b = pair.b;

            CollisionData data = new CollisionData();

            if (IntersectionDetector.aabbInAABB(a.getAABB(), b.getAABB(), data)) {

                collidingPairs.add(pair);

                a.addCollidingObject(b);
                b.addCollidingObject(a);

                resolveCollision((PhysicsBody) a.getParent(), (PhysicsBody) b.getParent(), data);

            } else {

                a.removeCollidingObject(b);
                b.removeCollidingObject(a);

            }

        }

        //region Compare if objects just entered into collision, are staying in it, or exited

        for (Pair pair : collidingPairs) {

            if (!lastCollidingPairs.contains(pair)) {
                ((PhysicsBody) pair.a.getParent()).onCollisionEnter(pair.b.getParent());
                ((PhysicsBody) pair.b.getParent()).onCollisionEnter(pair.a.getParent());
            } else {
                ((PhysicsBody) pair.a.getParent()).onCollision(pair.b.getParent());
                ((PhysicsBody) pair.b.getParent()).onCollision(pair.a.getParent());
            }

        }

        for (Pair pair : lastCollidingPairs) {

            if (!collidingPairs.contains(pair)) {
                ((PhysicsBody) pair.a.getParent()).onCollisionExit(pair.b.getParent());
                ((PhysicsBody) pair.b.getParent()).onCollisionExit(pair.a.getParent());
            }

        }

        // Set lastCollidingPairs array to collidingPairs
        ArrayList<Pair> tmp = lastCollidingPairs;
        lastCollidingPairs = collidingPairs;
        collidingPairs = tmp;

        //endregion

    }

    // Resolves collision between A and B using impulse method
    private void resolveCollision(PhysicsBody a, PhysicsBody b, CollisionData data) {
        Vector2 relativeVelocity = a.getVelocity().sub(b.getVelocity());
        float relativeVelocityAlongNormal = relativeVelocity.dot(data.normal);

        // Objects are separating, no impulse required
        if (relativeVelocityAlongNormal > 0) {
            return;
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
        float totalInverseMass = a.getInverseMass() + b.getInverseMass();
        if (totalInverseMass > 0) {
            Vector2 correction = data.normal.mul((data.penetration.dot(data.normal) + 1) / totalInverseMass);

            if (data.normal.equals(Vector2.LEFT) || data.normal.equals(Vector2.RIGHT))
                correction = data.normal.mul((data.penetration.dot(data.normal) + 0) / totalInverseMass);

            if (a.getMass() > 0) a.setPosition(a.getPosition().add(correction.mul(a.getInverseMass())));
            if (b.getMass() > 0) b.setPosition(b.getPosition().sub(correction.mul(b.getInverseMass())));
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

                if (a.getAABB().getMin().x > b.getAABB().getMax().x) continue;

                if (possiblePairs.contains(pair)) continue;

                possiblePairs.add(pair);

            }
        }

        return possiblePairs;
    }

}