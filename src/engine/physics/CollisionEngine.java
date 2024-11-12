package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.physics.shapes.AABB;
import engine.physics.shapes.Circle;
import engine.physics.shapes.CollisionShape;

import java.util.ArrayList;
import java.util.Arrays;

public class CollisionEngine {

    private Engine engine;

    private ArrayList<Collider> colliders = new ArrayList<>();

    private ArrayList<CollisionPair> collisionPairs = new ArrayList<>();

    public CollisionEngine(Engine engine) {
        this.engine = engine;
    }

    public void update(float delta) {

        //TODO: Check for collisions

        for (Collider colliderMain : colliders) {

            // TODO: Check for collisions where main collision shape is an AABB

            boolean collide = false;

            if (colliderMain.getCollisionShape() instanceof AABB) {
                for (Collider collider : colliders) {

                    // Avoid checking collision with itself
                    if (colliderMain == collider)
                        continue;

                    if (collider.getCollisionShape() instanceof AABB) {
                        collide = IntersectionDetector.aabbInAABB((AABB) colliderMain.getCollisionShape(), (AABB) collider.getCollisionShape());
                    }

                    if (collider.getCollisionShape() instanceof Circle) {
                        collide = IntersectionDetector.aabbInCircle((AABB)colliderMain.getCollisionShape(), (Circle)collider.getCollisionShape());
                    }

                    if (collide) {

                        // Collision logic
                        CollisionPair pair = new CollisionPair(colliderMain, collider);

                        if (!isPairInArray(collisionPairs, pair)) {

                            collisionPairs.add(pair);

                            colliderMain.setColliding(true);
                            colliderMain.getCollidingObjects().add(collider);

                            collider.setColliding(true);
                            collider.getCollidingObjects().add(colliderMain);

                        }

                    } else {

                        CollisionPair pair = new CollisionPair(colliderMain, collider);
                        if(isPairInArray(collisionPairs, pair)) {

                            pair.getA().setColliding(false);
                            pair.getB().setColliding(false);

                            pair.getA().getCollidingObjects().remove(pair.getB());
                            pair.getB().getCollidingObjects().remove(pair.getA());

                            collisionPairs.removeIf(p -> CollisionPair.compare(pair, p));
                        }

                    }

                }
            } else if (colliderMain.getCollisionShape() instanceof Circle) {

                // TODO: Check for collisions where main collision shape is a circle

                for (Collider collider : colliders) {

                    // Avoid checking collision with itself
                    if (colliderMain == collider)
                        continue;

                    if (collider.getCollisionShape() instanceof AABB) {
                        collide = IntersectionDetector.circleInAABB((Circle) colliderMain.getCollisionShape(), (AABB) collider.getCollisionShape());
                    }

                    if (collider.getCollisionShape() instanceof Circle) {
                        collide = IntersectionDetector.circleInCircle((Circle)colliderMain.getCollisionShape(), (Circle)collider.getCollisionShape());
                    }

                    if (collide) {

                        // Collision logic
                        CollisionPair pair = new CollisionPair(colliderMain, collider);

                        if (!isPairInArray(collisionPairs, pair)) {

                            collisionPairs.add(pair);

                            colliderMain.setColliding(true);
                            colliderMain.getCollidingObjects().add(collider);

                            collider.setColliding(true);
                            collider.getCollidingObjects().add(colliderMain);

                        }

                    } else {

                        CollisionPair pair = new CollisionPair(colliderMain, collider);
                        if(isPairInArray(collisionPairs, pair)) {

                            pair.getA().setColliding(false);
                            pair.getB().setColliding(false);

                            pair.getA().getCollidingObjects().remove(pair.getB());
                            pair.getB().getCollidingObjects().remove(pair.getA());

                            collisionPairs.removeIf(p -> CollisionPair.compare(pair, p));
                        }

                    }

                }
            }

        }

    }

    private static boolean isPairInArray(ArrayList<CollisionPair> pairs, CollisionPair pair) {
        for (CollisionPair p : pairs) {
            if (CollisionPair.compare(p, pair)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Collider> getColliders() {
        return colliders;
    }

    public void addCollider(Collider ...c) {
        colliders.addAll(Arrays.asList(c));
    }

    public void removeCollider(Collider collider) {
        colliders.remove(collider);
    }

}
