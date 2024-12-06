package engine.objects;

import engine.Engine;
import engine.physics.Vector2;

public class Camera extends GameObject {

    private GameObject reference;
    private float speed = 1.0f;
    private boolean current = true;

    //region Getters & Setters

    public void setReference(GameObject reference) {
        this.reference = reference;
    }

    public GameObject getReference() {
        return reference;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    //endregion

    public Camera(GameObject parent, GameObject reference, String name) {
        super(parent, name, Vector2.ZERO);
        this.reference = reference;
    }

    @Override
    public void update(Engine engine, float delta) {
        followReference(engine, delta);
        super.update(engine, delta);
    }

    private void followReference(Engine engine, float delta) {
        if (reference == null) return;


    }

}
