package engine.physics.shapes;

import engine.objects.GameObject;

public class CollisionShape {

    protected GameObject body = null;

    public CollisionShape() {}

    public CollisionShape(GameObject body) {
        this.body = body;
    }

}
