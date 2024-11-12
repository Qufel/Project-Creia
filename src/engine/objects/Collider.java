package engine.objects;

import engine.physics.CollisionEngine;
import engine.physics.Vector2;
import engine.physics.shapes.CollisionShape;

import java.util.ArrayList;

public class Collider extends GameObject {

    private CollisionEngine cEngine;

    private CollisionShape collisionShape = null;

    private boolean isColliding = false;
    private ArrayList<GameObject> collidingObjects = new ArrayList<>();

    public Collider(GameObject parent, String name, Vector2 position, CollisionShape shape, CollisionEngine cEngine) {
        super(parent, name, position);

        this.collisionShape = shape;
        this.cEngine = cEngine;

        this.collisionShape.setBody(this);

        cEngine.addCollider(this);
    }

    @Override
    public void decompose() {
        super.decompose();
        collidingObjects.clear();

        cEngine.removeCollider(this);
    }

    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

    public ArrayList<GameObject> getCollidingObjects() {
        return collidingObjects;
    }
}
