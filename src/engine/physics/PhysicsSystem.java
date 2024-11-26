package engine.physics;

import engine.objects.PhysicsBody;
import engine.physics.forces.ForceRegistry;
import engine.physics.forces.Gravity;

import java.util.ArrayList;

public class PhysicsSystem {
    private ForceRegistry forceRegistry;
    private ArrayList<PhysicsBody> bodies;

    public Gravity gravity;
    private float fixedDelta;

    public ForceRegistry getForceRegistry() {
        return forceRegistry;
    }

    public PhysicsSystem(float fixedDelta, Vector2 gravity) {
        this.forceRegistry = new ForceRegistry();
        this.bodies = new ArrayList<>();
        this.gravity = new Gravity(gravity);
        this.fixedDelta = fixedDelta;
    }

    public void update(float delta) {
        fixedUpdate();
    }

    public void fixedUpdate() {
        forceRegistry.updateForces(fixedDelta);

        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).physicsUpdate(fixedDelta);
        }
    }

    public void addBody(PhysicsBody body) {

        if (body.isFrozen()) return;

        this.bodies.add(body);
        if (body.getMass() != 0.0) {
            this.forceRegistry.add(body, gravity);
        }
    }

    public void clearBodies() {
        this.bodies.clear();
    }

}
