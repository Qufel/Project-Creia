package engine.objects;

import engine.Engine;
import engine.physics.forces.ForceGenerator;
import engine.physics.Vector2;

public class StaticBody extends PhysicsBody {

    public StaticBody(GameObject parent, String name, Vector2 position) {
        super(parent, name, position);
        this.setFreeze(true);
    }

    @Override
    public void start(Engine engine) {

    }

    @Override
    public void update(Engine engine, float delta) {

    }
}
