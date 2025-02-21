package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.physics.collisions.*;
import engine.physics.shapes.AABB;

import java.lang.reflect.Array;
import java.util.ArrayList;

import engine.Renderer;
import engine.graphics.Color;

/* TODO
    - Continuous Collision Detection (CCD)
 */


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

    public void update(float delta) {

        possiblePairs = getPossiblePairs(delta);

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

                // ENTERS COLLISION

                PhysicsBody a = (PhysicsBody) pair.a.getParent();
                PhysicsBody b = (PhysicsBody) pair.b.getParent();

                a.onCollisionEnter(b);
                b.onCollisionEnter(a);

                // Add collision normal
                pair.a.addCollisionNormal(CollisionData.getNormal(pair.b, pair.a));
                pair.b.addCollisionNormal(CollisionData.getNormal(pair.a, pair.b));

            } else {

                // STAYS IN COLLISION

                PhysicsBody a = (PhysicsBody) pair.a.getParent();
                PhysicsBody b = (PhysicsBody) pair.b.getParent();

                a.onCollision(b);
                b.onCollision(a);

            }

        }

        for (Pair pair : lastCollidingPairs) {

            if (!collidingPairs.contains(pair)) {

                // EXITS COLLISION

                ((PhysicsBody) pair.a.getParent()).onCollisionExit(pair.b.getParent());
                ((PhysicsBody) pair.b.getParent()).onCollisionExit(pair.a.getParent());

                //  Remove collision normal
                pair.a.removeCollisionNormal(CollisionData.getNormal(pair.b, pair.a));
                pair.b.removeCollisionNormal(CollisionData.getNormal(pair.a, pair.b));
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

    private ArrayList<Pair> getPossiblePairs(float delta) {
        ArrayList<Pair> pairs = new ArrayList<Pair>();

        // TODO: Broad-phase pass even though objects are not close or are far away from each other.

        for (Collider a : colliders) {
            if (a == null) continue;

            for (Collider b : colliders) {

                if (b == null || a.equals(b)) continue;
                
                PhysicsBody aBody = (PhysicsBody) a.getParent();
                PhysicsBody bBody = (PhysicsBody) b.getParent();

                if (aBody.equals(bBody)) continue;  
                if (aBody.getVelocity().equals(Vector2.ZERO) && bBody.getVelocity().equals(Vector2.ZERO)) continue;

                if (a.getAABB().getMin().x > b.getAABB().getMax().x) continue;

                Pair pair = new Pair(a, b);

                if (pairs.contains(pair)) continue;

                pairs.add(pair);

            }
        }

        return pairs;
    }

}