package engine.objects;

import engine.Vector2;

public class PhysicsBody extends GameObject {

    private double mass = 1.0;
    private double gravityScale = 1.0;
    private Vector2 velocity;

    private boolean freeze = false;

    public PhysicsBody(Vector2 position) {
        this.position = position;
    }

    public void updatePhysicsBody() {
        //TODO: Do physics updates
    }

}
