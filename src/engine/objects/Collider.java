package engine.objects;

import engine.Engine;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.util.ArrayList;

public class Collider extends GameObject {

    private AABB aabb = null;

    private boolean isColliding = false;
    private ArrayList<Collider> collidingObjects = new ArrayList<>();

    public Collider(GameObject parent, String name, Vector2 position, AABB aabb) {
        super(parent, name, position);

        this.aabb = aabb;

        this.aabb.setCollider(this);
    }

    @Override
    public void update(Engine engine, float delta) {
        if (!collidingObjects.isEmpty()) isColliding = true;
        else isColliding = false;
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
        return isColliding;
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
}
