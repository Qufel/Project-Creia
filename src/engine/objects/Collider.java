package engine.objects;

import engine.physics.CollisionEngine;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.util.ArrayList;

public class Collider extends GameObject {

    private CollisionEngine cEngine;

    private AABB aabb = null;

    private boolean isColliding = false;
    private ArrayList<Collider> collidingObjects = new ArrayList<>();

    public Collider(GameObject parent, String name, Vector2 position, AABB aabb, CollisionEngine cEngine) {
        super(parent, name, position);

        this.aabb = aabb;
        this.cEngine = cEngine;

        this.aabb.setCollider(this);

        cEngine.addCollider(this);
    }

    @Override
    public void decompose() {
        collidingObjects.clear();
        cEngine.removeCollider(this);

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
