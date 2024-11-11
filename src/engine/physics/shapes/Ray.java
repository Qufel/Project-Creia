package engine.physics.shapes;

import engine.physics.Vector2;

public class Ray {

    private Vector2 origin;

    private Vector2 direction;

    public Ray(Vector2 origin, Vector2 direction) {
        this.origin = origin;

        this.direction = direction;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public Vector2 getDirection() {
        return direction;
    }

}
