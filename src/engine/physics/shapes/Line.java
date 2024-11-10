package engine.physics.shapes;

import engine.physics.Vector2;

public class Line {

    private Vector2 start = null;
    private Vector2 end = null;

    public Line(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
    }

    public double getSquaredLength() {
        return start.x * start.x + end.x * end.x;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public double getA() {

        int dx = end.x - start.x;
        int dy = end.y - start.y;

        return dx == 0 ? 0.0 : (double) dy / (double) dx;

    }

    public double getB() {

        return end.y - this.getA() * end.x;

    }

}
