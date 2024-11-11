package engine.objects;

import engine.physics.Vector2;
import engine.physics.shapes.CollisionShape;

import java.util.ArrayList;

public class Collider extends GameObject {

    private CollisionShape collisionShape = null;

    private boolean isColiding = false;
    private ArrayList<GameObject> collidingWith = new ArrayList<GameObject>();

    public Collider(GameObject parent, String name, Vector2 position) {

        super(parent, name, position);

    }
}
