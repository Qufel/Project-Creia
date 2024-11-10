package engine.physics.shapes;

import engine.objects.GameObject;
import engine.physics.Vector2;

public class CollisionShape {

    protected GameObject body = null;

    public CollisionShape() {}

    public CollisionShape(GameObject body) {
        this.body = body;
    }

    public Vector2 getCenter() {
        return body.getGlobalPosition();
    }

}
