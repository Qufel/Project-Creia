package engine.objects;

import engine.physics.Physics;
import engine.physics.Vector2;

public class PhysicsBody extends GameObject {

    private Physics physics;

    public PhysicsBody(GameObject parent, String name, Vector2 position, Physics physics) {
        super(parent, name, position);

        this.physics = physics;
        physics.addPhysicsBody(this);
    }

    @Override
    public void decompose() {
        super.decompose();
        physics.removePhysicsBody(this);
    }
}
