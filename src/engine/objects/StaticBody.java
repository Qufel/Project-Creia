package engine.objects;

import engine.physics.ForceGenerator;
import engine.physics.Vector2;

public class StaticBody extends GameObject implements ForceGenerator {

    private double friction = 0.0;

    public StaticBody(GameObject parent, String name, Vector2 position) {
        super(parent, name, position);
    }

    @Override
    public void updateForce(float delta) {

    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }
}
