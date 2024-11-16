package engine.physics;

import engine.Engine;
import engine.objects.Collider;
import engine.objects.PhysicsBody;
import engine.objects.StaticBody;

import java.util.ArrayList;
import java.util.Arrays;

public class PhysicsEngine {

    private Engine engine;
    private CollisionEngine collisionEngine;

    private ArrayList<PhysicsBody> physicsBodies = new ArrayList<>();

    public final Vector2 GRAVITY = new Vector2(0, -10);

    public PhysicsEngine(Engine engine, CollisionEngine collisionEngine) {
        this.engine = engine;
        this.collisionEngine = collisionEngine;
    }

    // Update physics
    public void update(float delta) {

        //FIXME: Falling through collider for no reason

        //TODO: Friction
        //TODO: Block movement and add counter forces on other normals

        for (PhysicsBody pb : physicsBodies) {

            Collider collider = pb.getCollider();

            pb.onGround(false);
            pb.addForce(new Vector2(GRAVITY));

            Vector2 frictionForce = Vector2.ZERO;

            if (collider.isColliding()) {
                for (Collider c : collider.getCollidingObjects()) {
                    ArrayList<Vector2> normals = collisionEngine.getNormalsOfCollision(collider, c);
                    for (Vector2 n : normals) {
                        if (Vector2.compare(Vector2.UP, n)) {
                            pb.onGround(true);
                        }
                    }
                }
            }

//            pb.addForce(frictionForce);

            if (pb.isOnGround()) {
                pb.addForce(new Vector2(GRAVITY).mul(-1));

                pb.setVelocity(pb.getVelocity().mul(Vector2.RIGHT));
                pb.setAcceleration(pb.getAcceleration().mul(Vector2.RIGHT));
            }

            pb.sumForces();
            pb.updateForce(delta);
            pb.clearForces();

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
