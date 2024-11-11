package engine.physics.shapes;

import engine.physics.Vector2;

public class RaycastResult {

    private Vector2 point;
    private double[] normal;

    private float t;
    private boolean hit;

    public RaycastResult() {
        this.point = new Vector2();
        this.normal = new double[2];
        this.t = -1.0f;
        this.hit = false;
    }

    public void init(Vector2 point, double[] normal, float t, boolean hit) {
        this.point = point;
        this.normal = normal;
        this.t = t;
        this.hit = hit;

    }

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.point = Vector2.ZERO;
            result.normal = new double[2];
            result.t = -1.0f;
            result.hit = false;
        }
    }

}
