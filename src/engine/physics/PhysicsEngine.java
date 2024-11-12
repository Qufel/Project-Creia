package engine.physics;

import engine.Engine;
import engine.objects.PhysicsBody;

import java.util.ArrayList;
import java.util.Arrays;

public class PhysicsEngine {

    private Engine engine;

    private ArrayList<PhysicsBody> physicsBodies = new ArrayList<>();

    public final Vector2 GRAVITY = new Vector2(0, -6);

    public PhysicsEngine(Engine engine) {
        this.engine = engine;
    }

    // Update physics
    public void update(float delta) {

        for (PhysicsBody pb : physicsBodies) {

            //TODO: Calculate forces for every PhysicsBody and apply them

        }

    }

    //region PhysicsBodies management

    public void addPhysicsBody(PhysicsBody ...bodies) {
        physicsBodies.addAll(Arrays.asList(bodies));
    }

    public void removePhysicsBody(PhysicsBody body) {
        physicsBodies.remove(body);
    }

    public ArrayList<PhysicsBody> getPhysicsBodies() {
        return physicsBodies;
    }

    //endregion
}
