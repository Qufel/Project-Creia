package engine.objects;

import engine.Engine;
import engine.physics.Vector2;
import engine.physics.collisions.CollisionNormal;
import engine.physics.shapes.AABB;

import java.util.ArrayList;

public class Collider extends GameObject {

    private AABB aabb = null;

    private boolean isColliding = false;

    // Colliders that are currently colliding with collider
    private final ArrayList<Collider> collidingObjects = new ArrayList<>();

    private final ArrayList<Vector2> collisionNormals = new ArrayList<>();

    public Collider(GameObject parent, String name, Vector2 position, AABB aabb) {
        super(parent, name, position);

        this.aabb = aabb;
        this.aabb.setCollider(this);
    }

    @Override
    public void update(Engine engine, float delta) {

    }

    @Override
    public void decompose() {

        for (Collider object : collidingObjects) {
            object.getCollidingObjects().remove(this);
        }

        collidingObjects.clear();
        isColliding = false;
        super.decompose();
    }

    public AABB getAABB() {
        return aabb;
    }

    public AABB getFutureAABB(float delta) {
        AABB futureAABB = new AABB(this.getAABB());
        Vector2 displacement = ( (PhysicsBody) this.getParent()).getVelocity().mul(delta); 
        futureAABB.move(displacement);
        return futureAABB;
    }

    public boolean isColliding() {
        return !collidingObjects.isEmpty();
    }

    public boolean isCollidingWith(GameObject object) {
        return getCollidingObjects().contains((Collider) object);
    }

    public void setColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

    //region CollisionNormals

    public void addCollisionNormal(Vector2 normal) {
        if (!collisionNormals.contains(normal)) {
            collisionNormals.add(normal);
        }
    }

    public void removeCollisionNormal(Vector2 normal) {
        collisionNormals.remove(normal);
    }

    public ArrayList<Vector2> getCollisionNormals() {
        return collisionNormals;
    }

    //endregion

    //region CollidingObjects

    public ArrayList<Collider> getCollidingObjects() {
        return collidingObjects;
    }

    public void addCollidingObject(Collider collider) {
        if (collider != null) {
            if (!collidingObjects.contains(collider)) {
                collidingObjects.add(collider);
            }
        }
    }

    public void removeCollidingObject(Collider collider) {
        if (collider != null) {
            collidingObjects.remove(collider);
        }
    }

    //endregion
}
