package engine.objects;

import engine.physics.Vector2;

import java.util.ArrayList;

public class PhysicsBody extends GameObject {

    private double mass; // The body's mass
    private double inverseMass = 0.0;

    private double gravityScale = 1.0; // A scale in which gravity acts with this body i.e. how much is affected by it

    private Vector2 velocity = new Vector2(0, 0);
    private Vector2 acceleration = new Vector2(0, 0);

    private ArrayList<Vector2> forces = new ArrayList<>();
    private Vector2 finalForce = new Vector2(0, 0);

    private boolean collide = true; // If false ignore all collisions
    private boolean freeze = false; // Is body frozen in physics simulation i.e. is affected by physics
    private boolean onGround = false;

    //region Getters & Setters

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public ArrayList<Vector2> getForces() {
        return forces;
    }

    public Vector2 getFinalForce() {
        return finalForce;
    }

    public double getMass() {
        return mass;
    }

    public double getInverseMass() {
        return inverseMass;
    }

    public void setMass(double mass) {
        this.mass = mass;

        if (mass != 0.0)
            inverseMass = 1.0 / mass;
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

    public boolean isFrozen() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public void onGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return onGround;
    }

    //endregion

    public PhysicsBody(GameObject parent, String name, Vector2 position) {
        super(parent, name, position);
    }

    public void physicsUpdate(float delta) {
        if (mass == 0.0) return; //If mass is 0.0 then it's unaffected by physics

        this.acceleration = new Vector2(finalForce).mul(inverseMass);
        this.velocity = this.velocity.add(acceleration.mul(delta * 10));

        this.setPosition(this.getPosition().add(velocity.mul(delta * 10)));

        clearForce();
    }


    @Override
    public void decompose() {
        super.decompose();
    }

    //region Forces

    public void addForce(Vector2 force) {
        this.finalForce = this.finalForce.add(force);
    }

    public void clearForce() {
        finalForce = Vector2.ZERO;
    }

    //endregion

    //region Colliders

    public Collider getCollider() {
        for (GameObject child : getChildren()) {
            if (child instanceof Collider) {
                return (Collider) child;
            }
        }

        throw new NullPointerException("PhysicsBody should have Collider but found null!");
    }

    //endregion

}
