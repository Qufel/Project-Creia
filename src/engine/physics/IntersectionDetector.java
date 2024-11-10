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
}
