package engine.physics.forces;

import engine.objects.PhysicsBody;
import engine.physics.Vector2;

public class Gravity implements ForceGenerator {

    private Vector2 gravity;

    public Gravity(Vector2 force) {
        this.gravity = new Vector2(force);
    }

    @Override
    public void updateForce(PhysicsBody body, float delta) {
        body.addForce(this.gravity);
    }
}
