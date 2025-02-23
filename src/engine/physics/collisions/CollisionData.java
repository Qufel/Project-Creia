package engine.physics.collisions;

import engine.objects.Collider;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

public class CollisionData {
    public Vector2 penetration = Vector2.ZERO;
    public Vector2 normal = Vector2.ZERO;
    public Vector2 point = Vector2.ZERO;

    private static double getAngle(Vector2 v) {
        return Math.atan2(v.y, v.x);
    }

}
