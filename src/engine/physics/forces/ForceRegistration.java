package engine.physics.forces;

import engine.objects.PhysicsBody;

public class ForceRegistration {
    public ForceGenerator generator;
    public PhysicsBody body;

    public ForceRegistration(ForceGenerator generator, PhysicsBody body) {
        this.generator = generator;
        this.body = body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ForceRegistration)) return false;

        ForceRegistration other = (ForceRegistration) obj;
        return other.generator.equals(generator) && other.body.equals(body);

    }
}
