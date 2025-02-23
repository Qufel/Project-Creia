package engine.physics.collisions;

import engine.physics.Vector2;
import engine.physics.shapes.*;

import java.awt.*;

public class IntersectionDetector {

    //region Point

    public static boolean pointOnLine(Line line, Vector2 point) {

        double dx = line.getEnd().x - line.getStart().x;
        double dy = line.getEnd().y - line.getStart().y;

        if (dx == 0) {
            return point.x == line.getStart().x;
        }

        double a = (double) dy / (double) dx;
        double b = line.getEnd().y - a * line.getEnd().x;

        return (double) point.y == (double) a * point.x + b;

    }

    public static boolean pointInAABB(AABB aabb, Vector2 point) {

        Vector2 min = aabb.getMin();
        Vector2 max = aabb.getMax();

        return point.x <= max.x && min.x <= point.x
                && point.y <= max.y && min.y <= point.y;

    }

    //endregion

    //region Line

    public static boolean lineInAABB(AABB aabb, Line line) {

        if (pointInAABB(aabb, line.getEnd()) || pointInAABB(aabb, line.getStart())) {
            return true;
        }

        //region Workaround for perfectly straight lines

        // Vertical lines
        if (line.getEnd().y == line.getStart().y) {

            int smallestX = Math.min(line.getStart().x, line.getEnd().x);
            int biggestX = Math.max(line.getStart().x, line.getEnd().x);

            if (line.getStart().y <= aabb.getMax().y && line.getStart().y >= aabb.getMin().y
                && smallestX <= aabb.getMin().x && biggestX >= aabb.getMax().x) {
                return true;
            }

        }

        // Horizontal lines

        if (line.getEnd().x == line.getStart().x) {

            int smallestY = Math.min(line.getStart().y, line.getEnd().y);
            int biggestY = Math.max(line.getStart().y, line.getEnd().y);

            if (line.getStart().x <= aabb.getMax().x && line.getStart().x >= aabb.getMin().x
                    && smallestY <= aabb.getMin().y && biggestY >= aabb.getMax().y) {
                return true;
            }

        }

        //endregion

        float unitX = new Vector2(line.getEnd()).sub(line.getStart()).x;
        float unitY = new Vector2(line.getEnd()).sub(line.getStart()).y;

        unitX = (unitX == 0f) ? Float.MIN_VALUE : unitX;
        unitY = (unitY == 0f) ? Float.MIN_VALUE : unitY;

        float magnitude = (float) Math.sqrt(unitX * unitX + unitY * unitY);

        unitX /= magnitude;
        unitY /= magnitude;

        unitX = (unitX != 0) ? 1.0f / unitX : 0.0f;
        unitY = (unitY != 0) ? 1.0f / unitY : 0.0f;

        Vector2 min = aabb.getMin();
        min = min.sub(line.getStart());
        min.x *= unitX;
        min.y *= unitY;

        Vector2 max = aabb.getMax();
        max = max.sub(line.getStart());
        max.x *= unitX;
        max.y *= unitY;

        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if (tMax < 0 || tMin > tMax) {
            return false;
        }

        float t = (tMin < 0) ? tMax : tMin;

        return t > 0f && t * t < line.getSquaredLength();
    }

    //endregion

    //region AABB

    public static boolean aabbInAABB(AABB b1, AABB b2) {
        return overlapOnAxis(b1, b2, Vector2.RIGHT) && overlapOnAxis(b2, b1, Vector2.UP);
    }

    public static boolean aabbInAABB(AABB b1, AABB b2, CollisionData data) {

        boolean colliding = false;

        Vector2 aX = getInterval(b1, Vector2.RIGHT);
        Vector2 bX = getInterval(b2, Vector2.RIGHT);

        Vector2 aY = getInterval(b1, Vector2.UP);
        Vector2 bY = getInterval(b2, Vector2.UP);

        // Check for overlap
        if ( (bX.x <= aX.y && aX.x <= bX.y) && (bY.x <= aY.y && aY.x <= bY.y) ) {

            // Get penetration on each axis
            int penetrationY = Math.min(aY.y - bY.x, bY.y - aY.x);
            int penetrationX = Math.min(aX.y - bX.x, bX.y - aX.x);

            Vector2 normal = getNormal(b1, b2); // Get normal of collision

            if (normal.equals(Vector2.ZERO))
                throw new RuntimeException("Collision normal vector is zero!");

            colliding = true;
            setData(data, normal, new Vector2(penetrationX, penetrationY));
        }

        return colliding;

    }

    private static void setData(CollisionData data, Vector2 normal, Vector2 penetration) {
        data.normal = normal;
        data.penetration = penetration;
    }

    private static boolean overlapOnAxis(AABB b1, AABB b2, Vector2 axis) {
        Vector2 res1 = getInterval(b1, axis);
        Vector2 res2 = getInterval(b2, axis);

        return res2.x <= res1.y && res1.x <= res2.y;

    }

    private static double getAngle(Vector2 v) {
        return Math.atan2(v.y, v.x);
    }


    private static Vector2 getInterval(AABB aabb, Vector2 axis) {
        Vector2 min = aabb.getMin();
        Vector2 max = aabb.getMax();

        Vector2 result = new Vector2(0, 0); // x is minimum value of projection on axis and y is maximum value

        Vector2[] verts = {
                new Vector2(min),
                new Vector2(min.x, max.y),
                new Vector2(max),
                new Vector2(max.x, min.y),
        };

        // In order to get max and min calculate a dot product of vertex and axis

        double smallestDot = Float.MAX_VALUE, biggestDot = Float.MIN_VALUE;
        for (Vector2 v : verts) {
            double dot = v.dot(axis);

            if (dot < smallestDot) {
                smallestDot = dot;
            }

            if (dot > biggestDot) {
                biggestDot = dot;
            }

        }

        result.x = (int) smallestDot;
        result.y = (int) biggestDot;

        return result;
    }

    //endregion

    //region Raycast

    //endregion

    public static Vector2 getNormal(AABB a, AABB b) {

        Vector2 aX = getInterval(a, Vector2.RIGHT);
        Vector2 bX = getInterval(b, Vector2.RIGHT);

        Vector2 aY = getInterval(a, Vector2.UP);
        Vector2 bY = getInterval(b, Vector2.UP);

        // Check for overlap
        if ( (bX.x <= aX.y && aX.x <= bX.y) && (bY.x <= aY.y && aY.x <= bY.y) ) {

            // Get penetration on each axis
            int penetrationY = Math.min(aY.y - bY.x, bY.y - aY.x);
            int penetrationX = Math.min(aX.y - bX.x, bX.y - aX.x);

            if (penetrationY < penetrationX) { // Normal in Y axis

                int ab = aY.y - bY.y + aY.x - bY.x;

                if (ab < 0) {
                    return Vector2.UP;
                } else {
                    return Vector2.DOWN;
                }

            } else { // Normal in X axis

                int ab = aX.y - bX.y + aX.x - bX.x;

                if (ab < 0) {
                    return Vector2.LEFT;
                } else {
                    return Vector2.RIGHT;
                }

            }

        }

        return Vector2.ZERO;
    }

}
