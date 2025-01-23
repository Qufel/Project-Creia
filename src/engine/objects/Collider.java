package engine.objects;

import engine.Engine;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.util.ArrayList;

public class Collider extends GameObject {

    private AABB aabb = null;

    private boolean isColliding = false;

    // Colliders that are currently colliding with collider
    private final ArrayList<Collider> collidingObjects = new ArrayList<>();

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

    public boolean isColliding() {
        return !collidingObjects.isEmpty();
    }

    public boolean isCollidingWith(GameObject object) {
        return getCollidingObjects().contains((Collider) object);
    }

    public void setColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

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

}
