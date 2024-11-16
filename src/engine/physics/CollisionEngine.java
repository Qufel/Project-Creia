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

        for (CollisionInfo collisionInfo : collisionInfos) {
            AABB a = collisionInfo.getA().getAABB();
            AABB b = collisionInfo.getB().getAABB();

            collisionInfo.setCollisionNormals(getCollisionNormal(a, b));
            getNormalsOfCollision(collisionInfo.getA(), collisionInfo.getB());
        }

    }

    private void manageCollision(Collider colliderMain, Collider collider, boolean collide) {
        if (collide) {

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

    private static int getCollisionNormal(AABB a, AABB b) {

        int normals = 0b0000;

        Vector2 minToMin = new Vector2(a.getMin()).sub(b.getMin());
        Vector2 maxToMax = new Vector2(a.getMax()).sub(b.getMax());

        //0b1001

        // A collides with B on the right
        if (a.getMin().x >= b.getMax().x) {
            normals = normals | (0b1 << 2);
        }
        // A collides with B on the left
        if (a.getMax().x <= b.getMin().x) {
            normals = normals | (0b1);
        }
        // A collides with B from the top
        if (a.getMin().y >= b.getMax().y) {
            normals = normals | (0b1 << 1);
        }
        // A collides with B from the bottom
        if (a.getMax().y <= b.getMin().y) {
            normals = normals | (0b1 << 3);
        }

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

    private CollisionInfo getCollisionInfoFromList(Collider a, Collider b) {
        if (isPairInArray(collisionInfos, new CollisionInfo(a, b))) {
            for (CollisionInfo p : collisionInfos) {
                if ( (p.getA() == a && p.getB() == b) || (p.getA() == b && p.getB() == a) ) {
                    return p;
                }
            }
        }
        return null;
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

    public ArrayList<Vector2> getNormalsOfCollision(Collider a, Collider b) {
        CollisionInfo info = getCollisionInfoFromList(a, b);
        ArrayList<Vector2> normals = new ArrayList<>();

        if (info != null) {

            int n = info.getCollisionNormals();
            Vector2[] normalsBase = {Vector2.LEFT, Vector2.DOWN, Vector2.RIGHT, Vector2.UP};

            for (int i = 0; n > 0; i++) {

                if ((n & 1) == 1) {
                    normals.add(normalsBase[i]);
                }

                n >>= 1;
            }

            return normals;

        }
        return new ArrayList<>();
    }
}
