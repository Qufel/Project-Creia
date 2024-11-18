package engine.physics.forces;

import engine.objects.PhysicsBody;

public interface ForceGenerator {
    public void updateForce(PhysicsBody body, float delta);
}
