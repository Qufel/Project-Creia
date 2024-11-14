package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.physics.shapes.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CollisionEngine {

    private Engine engine;

    private ArrayList<Collider> colliders = new ArrayList<>();

    private ArrayList<CollisionInfo> collisionInfos = new ArrayList<>();

    public CollisionEngine(Engine engine) {
        this.engine = engine;
    }

    public void update(float delta) {

        //TODO: Check for collisions

        for (Collider colliderMain : colliders) {

            // TODO: Check for collisions where main collision shape is an AABB

            boolean collide = false;

            for (Collider collider : colliders) {

                // Avoid checking collision with itself
                if (colliderMain == collider)
                    continue;

                collide = IntersectionDetector.aabbInAABB((AABB) colliderMain.getAABB(), (AABB) collider.getAABB());
                manageCollision(colliderMain, collider, collide);

            }

        }

    }

    private void manageCollision(Collider colliderMain, Collider collider, boolean collide) {
        if (collide) {


            int normals = getCollisionNormal(colliderMain, collider);
            // Collision logic
            CollisionInfo pair = new CollisionInfo(colliderMain, collider);

            if (!isPairInArray(collisionInfos, pair)) {

                collisionInfos.add(pair);

                colliderMain.setColliding(true);
                colliderMain.getCollidingObjects().add(collider);

                collider.setColliding(true);
                collider.getCollidingObjects().add(colliderMain);

            }

        } else {

            CollisionInfo pair = new CollisionInfo(colliderMain, collider);
            if(isPairInArray(collisionInfos, pair)) {

                pair.getA().setColliding(false);
                pair.getB().setColliding(false);

                pair.getA().getCollidingObjects().remove(pair.getB());
                pair.getB().getCollidingObjects().remove(pair.getA());

                collisionInfos.removeIf(p -> CollisionInfo.compare(pair, p));
            }

        }
    }

    private static int getCollisionNormal(Collider main, Collider other) {

        int normals = 0b0000;

        return normals;
    }

    private static int collisionNormalsAABBandAABB(AABB b1, AABB b2) {

        int normals = 0b0000;

        // Get four sides of AABB as lines

        Line top = new Line(new Vector2(b2.getMin().x, b2.getMax().y), new Vector2(b2.getMax()));
        Line right = new Line(new Vector2(b2.getMax()), new Vector2(b2.getMax().x, b2.getMin().y));
        Line bottom = new Line(new Vector2(b2.getMin()), new Vector2(b2.getMax().x, b2.getMin().y));
        Line left = new Line(new Vector2(b2.getMin()), new Vector2(b2.getMax().x, b2.getMax().y));

        // Set bits for each collision

        normals = (normals & ~(0b1 << 3)) | ((IntersectionDetector.lineInAABB(b1, top) ? 1 : 0) << 3);
        normals = (normals & ~(0b1 << 2)) | ((IntersectionDetector.lineInAABB(b1, right) ? 1 : 0) << 2);
        normals = (normals & ~(0b1 << 1)) | ((IntersectionDetector.lineInAABB(b1, bottom) ? 1 : 0) << 1);
        normals = (normals & ~(0b1)) | ((IntersectionDetector.lineInAABB(b1, left) ? 1 : 0));

        return normals;
    }


    private static boolean isPairInArray(ArrayList<CollisionInfo> pairs, CollisionInfo pair) {
        for (CollisionInfo p : pairs) {
            if (CollisionInfo.compare(p, pair)) {
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
