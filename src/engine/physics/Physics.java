package engine.physics;

import engine.Engine;
import engine.objects.PhysicsBody;

import java.util.ArrayList;
import java.util.Arrays;

public class Physics {

    private Engine engine;

    private ArrayList<PhysicsBody> physicsBodies = new ArrayList<>();

    public final double GRAVITY = 9.81;

    public Physics(Engine engine) {
        this.engine = engine;
    }

    // ESSENTIAL function for updating game physics
    public void update() {

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
