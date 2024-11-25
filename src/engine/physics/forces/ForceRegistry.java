package engine.physics.forces;

import engine.objects.PhysicsBody;

import java.util.ArrayList;

public class ForceRegistry {
    private ArrayList<ForceRegistration> registry;

    public ForceRegistry() {
        registry = new ArrayList<>();
    }

    public void add(PhysicsBody body, ForceGenerator generator) {
        ForceRegistration registration = new ForceRegistration(generator, body);
        registry.add(registration);
    }

    public void remove(PhysicsBody body, ForceGenerator generator) {
        ForceRegistration registration = new ForceRegistration(generator, body);
        registry.remove(registration);
    }

    public void clear() {
        registry.clear();
    }

    public void updateForces(float delta) {
        for (ForceRegistration registration : registry) {
            registration.generator.updateForce(registration.body, delta);
        }
    }

    public ArrayList<ForceRegistration> getRegistryForBody(PhysicsBody body) {
        ArrayList<ForceRegistration> result = new ArrayList<>();
        for (ForceRegistration registration : registry) {
            if (registration.body == body) {
                result.add(registration);
            }
        }
        return result;
    }

    public void zeroForces() {
        for (ForceRegistration registration : registry) {
            registration.body.clearForce();
        }
    }
}
