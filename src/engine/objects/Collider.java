package engine.objects;

import engine.Engine;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.util.ArrayList;

public class Collider extends GameObject {

    private AABB aabb = null;

    private boolean isColliding = false;

    private ArrayList<Collider> collidingObjects = new ArrayList<>(); // Objects that are currently colliding with

    private ArrayList<Vector2> collisionPoints = new ArrayList<>(); // Directions from which collision occurs

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
        collidingObjects.clear();
        isColliding = false;
        super.decompose();
    }

    public AABB getAABB() {
        return aabb;
    }

    public boolean isColliding() {
        return !collidingObjects.isEmpty();
    }

    public boolean isCollidingWith(GameObject object) {
        return collidingObjects.contains(object);
    }

    public void setColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

    public ArrayList<Collider> getCollidingObjects() {
        return collidingObjects;
    }

    public void removeCollidingObject(Collider collider) {
        if (collider != null) {
            collidingObjects.remove(collider);

            ((PhysicsBody) collider.getParent()).onCollisionExit(this);

        }
    }

    public ArrayList<Vector2> getCollisionPoints() {
        return collisionPoints;
    }

    public void addCollisionPoint(Vector2 point) {
        if (!collisionPoints.contains(point))
            collisionPoints.add(point);
    }

    public void removeCollisionPoint(Vector2 point) {
        collisionPoints.remove(point);
    }

    public void clearCollisionPoints() {
        collisionPoints.clear();
    }

}
