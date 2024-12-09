package engine.objects;

import engine.Engine;
import engine.physics.Vector2;

public class Camera extends GameObject {

    private float offsetX, offsetY;

    private GameObject target;
    private float speed = 1.0f;
    private boolean current = true;

    //region Getters & Setters

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public GameObject getTarget() {
        return target;
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

    public Camera(GameObject parent, GameObject target, String name) {
        super(parent, name, Vector2.ZERO);
        this.target = target;
    }

    @Override
    public void update(Engine engine, float delta) {
        super.update(engine, delta);

        offsetX = target.getGlobalPosition().x - (float) engine.getWidth() / 2;
        offsetY = target.getGlobalPosition().y - (float) engine.getHeight() / 2;

        int x = 0, y = 0;

        if (offsetX < 0) {
            x = (int) Math.floor(offsetX);
        } else {
            x = (int) Math.ceil(offsetX);
        }

        if (offsetY < 0) {
            y = (int) Math.floor(offsetY);
        } else {
            y = (int) Math.ceil(offsetY);
        }


        engine.getRenderer().setCamera(new Vector2(x, y));
    }

}
