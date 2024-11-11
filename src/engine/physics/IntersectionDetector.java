package engine.physics;

import engine.physics.shapes.*;

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

    public static boolean pointInCircle(Circle circle, Vector2 point) {

        double d = new Vector2(circle.getCenter()).distanceSquared(point);

        return d <= circle.getRadius() * circle.getRadius();

    }

    public static boolean pointInAABB(AABB aabb, Vector2 point) {

        Vector2 min = aabb.getMin();
        Vector2 max = aabb.getMax();

        return point.x <= max.x && min.x <= point.x
                && point.y <= max.y && min.y <= point.y;

    }

    //endregion

    //region Line

    public static boolean lineInCircle(Circle circle, Line line) {

        Vector2 lineVector = new Vector2(line.getEnd()).sub(line.getStart());
        Vector2 vectorToCenter = new Vector2(circle.getCenter()).sub(line.getStart());

        double t = (vectorToCenter.dot(lineVector)) / (lineVector.dot(lineVector));

        if (t < 0.0 || t > 1.0) {
            return false;
        }

        Vector2 point = new Vector2(line.getStart()).add(new Vector2(lineVector).mul(t));

        return pointInCircle(circle, point);
    }

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

    //region Circle

    public static boolean circleInLine(Line line, Circle circle) {
        return lineInCircle(circle, line);
    }

    public static boolean circleInCircle(Circle c1, Circle c2) {

        Vector2 circleToCircleVector = new Vector2(c1.getCenter()).sub(new Vector2(c2.getCenter()));
        return Math.pow(c1.getRadius() + c2.getRadius(), 2) >= circleToCircleVector.lengthSquared();

    }

    public static boolean circleInAABB(Circle circle, AABB aabb) {
        Vector2 min = aabb.getMin();
        Vector2 max = aabb.getMax();

        Vector2 closestPointToCircle = new Vector2(circle.getCenter());

        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2 circleToAABB = new Vector2(circle.getCenter()).sub(closestPointToCircle);
        return circleToAABB.lengthSquared() <= circle.getRadius() * circle.getRadius();

    }

    //endregion

    //region AABB

    public static boolean aabbInCircle(AABB aabb, Circle circle) {
        return circleInAABB(circle, aabb);
    }

    public static boolean aabbInAABB(AABB b1, AABB b2) {
        return overlapOnAxis(b1, b2, Vector2.RIGHT) && overlapOnAxis(b2, b1, Vector2.UP);
    }

    private static boolean overlapOnAxis(AABB b1, AABB b2, Vector2 axis) {
        Vector2 res1 = getInterval(b1, axis);
        Vector2 res2 = getInterval(b2, axis);

        return res2.x <= res1.y && res1.x <= res2.y;

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
}
