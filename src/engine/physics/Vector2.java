package engine.physics;

public class Vector2 {

    public static final Vector2 DOWN = new Vector2(0,-1);
    public static final Vector2 UP = new Vector2(0,1);
    public static final Vector2 LEFT = new Vector2(-1,0);
    public static final Vector2 RIGHT = new Vector2(1,0);

    public int x, y;

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

    public double distanceTo(Vector2 v) {
        return Math.sqrt(Math.pow(this.x - v.x,2) + Math.pow(this.y - v.y,2));
    }

    public Vector2 subtract(Vector2 v) {
        return new Vector2(this.x - v.x,this.y - v.y);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x,this.y + v.y);
    }

}
