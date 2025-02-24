package engine.objects;

import engine.Engine;
import engine.physics.Vector2;

import java.util.ArrayList;

public class PhysicsBody extends GameObject {

    private float mass; // The body's mass
    private float inverseMass = 0.0f;

    private float gravityScale = 1.0f; // A scale in which gravity acts with this body i.e. how much is affected by it

    private Vector2 velocity = new Vector2(0, 0);
    private Vector2 acceleration = new Vector2(0, 0);

    private Vector2 finalForce = new Vector2(0, 0);

    private float friction = 0.5f;
    private float restitution = 0.5f;

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

    public Vector2 getFinalForce() {
        return finalForce;
    }

    public double getMass() {
        return mass;
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public void setMass(float mass) {
        this.mass = mass;

        if (mass != 0.0f)
            inverseMass = 1.0f / mass;
    }

    public double getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    //endregion

    public PhysicsBody(GameObject parent, String name, Vector2 position) {
        super(parent, name, position);
    }

    @Override
    public void update(Engine engine, float delta) {

        super.update(engine, delta);
    }

    public void physicsUpdate(float delta) {
        if (mass == 0.0) return; //If mass is 0.0 then it's unaffected by physics

        this.acceleration = new Vector2(finalForce).mul(inverseMass);
        this.velocity = this.velocity.add(acceleration.mul(delta * 10));

        this.setPosition(this.getPosition().add(velocity.mul(delta * 10)));

        clearForce();
    }

    public void onCollisionEnter(GameObject object) {

    }

    public void onCollisionExit(GameObject object) {

    }

    public void onCollision(GameObject object) {

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
