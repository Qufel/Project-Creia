package engine.objects;

import engine.physics.Physics;
import engine.physics.Vector2;

public class PhysicsBody extends GameObject {

    private Physics physics; // A link to physics engine

    private double mass; // The body's mass
    private double gravityScale = 1.0; // A scale in which gravity acts with this body i.e. how much is affected by it

    private boolean collide = true; // If false ignore all collisions
    private boolean freeze = false; // Is body frozen in physics simulation i.e. is affected by physics
    private boolean onGround = false;

    //region Getters & Setters

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(double gravityScale) {
        this.gravityScale = gravityScale;
    }

    public boolean isColliding() {
        return collide;
    }

    public void setColliding(boolean colliding) {
        this.collide = colliding;
    }

    public boolean isFrozend() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public boolean isOnGround() {
        return onGround;
    }

    //endregion

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
