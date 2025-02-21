package engine.physics;

import engine.EMath;
import engine.objects.PhysicsBody;

public class Vector2f extends Vector2 {

    public static final Vector2f UP = new Vector2f(0.0f, 1.0f);
    public static final Vector2f DOWN = new Vector2f(0.0f, -1.0f);
    public static final Vector2f LEFT = new Vector2f(-1.0f, 0.0f);
    public static final Vector2f RIGHT = new Vector2f(1.0f, 0.0f);

    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2f(Vector2 v) {
        this.x = (float) v.x;
        this.y = (float) v.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Vector2f sub(Vector2 v) {
        return new Vector2f(this.x - v.x,this.y - v.y);
    }

    public Vector2f add(Vector2 v) {
        return new Vector2f(this.x + v.x,this.y + v.y);
    }

    public Vector2f mul(float v) {
        return new Vector2f(this.x * v,this.y * v);
    }

    public Vector2f mul(Vector2 v) {
        return new Vector2f(this.x * v.x, this.y * v.y);
    }

    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }

    public Vector2f normalize() {
        float length = (float) Math.sqrt(this.lengthSquared());
        return new Vector2f(this.x / length, this.y / length);
    }

}
