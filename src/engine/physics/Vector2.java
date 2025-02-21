package engine.physics;

import engine.EMath;

public class Vector2 {

    public static final Vector2 DOWN = new Vector2(0,-1);
    public static final Vector2 UP = new Vector2(0,1);
    public static final Vector2 LEFT = new Vector2(-1,0);
    public static final Vector2 RIGHT = new Vector2(1,0);
    public static final Vector2 ZERO = new Vector2(0, 0);

    public int x, y;

    public Vector2() {}

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double distanceSquared(Vector2 v) {
        return Math.pow(this.x - v.x,2) + Math.pow(this.y - v.y,2);
    }

    public double lengthSquared() {
        return Math.pow(this.x,2) + Math.pow(this.y,2);
    }

    public Vector2 sub(Vector2 v) {
        return new Vector2(this.x - v.x,this.y - v.y);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x,this.y + v.y);
    }

    public Vector2 mul(double v) {
        return new Vector2(EMath.ceilFloor(this.x * v), EMath.ceilFloor(this.y * v));
    }

    public Vector2 mul(Vector2 v) {
        return new Vector2(EMath.ceilFloor(this.x * v.x), EMath.ceilFloor(this.y * v.y));
    }

    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }

    public Vector2 inverse() {
        return new Vector2(-this.x, -this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Vector2)) return false;

        Vector2 v = (Vector2)obj;
        return (float) v.x == (float) this.x && (float) v.y == (float) this.y;
    }

    public boolean larger(Vector2 v) {
        return v.x > this.x && v.y > this.y;
    }

}
