package engine.physics.collisions;

import engine.objects.Collider;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

public class CollisionData {
    public Vector2 penetration = Vector2.ZERO;
    public Vector2 normal = Vector2.ZERO;
    public Vector2 point = Vector2.ZERO;

    public static Vector2 getNormal(Collider a, Collider b) {

        AABB b1 = a.getAABB();
        AABB b2 = b.getAABB();

        double angle = getAngle(b1.getCenter().sub(b2.getCenter())) * -1;

        double angleTopRight = getAngle(b2.getMax().sub(b2.getCenter()));
        double angleTopLeft = getAngle(new Vector2(b2.getMin().x, b2.getMax().y).sub(b2.getCenter()));
        double angleBottomRight = getAngle(new Vector2(b2.getMax().x, b2.getMin().y).sub(b2.getCenter()));
        double angleBottomLeft = getAngle(b2.getMin().sub(b2.getCenter()));

        if (angle < 0) {

            if (angle < angleBottomLeft) {
                return Vector2.LEFT;
            } else if (angle > angleBottomRight) {
                return Vector2.RIGHT;
            } else {
                return Vector2.DOWN;
            }

        } else {

            if (angle < angleTopRight) {
                return Vector2.RIGHT;
            } else if (angle > angleTopLeft) {
                return Vector2.LEFT;
            } else {
                return Vector2.UP;
            }
        }
    }

    private static double getAngle(Vector2 v) {
        return Math.atan2(v.y, v.x);
    }

}
