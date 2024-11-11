package tests.physics;

import engine.objects.GameObject;
import engine.objects.Scene;
import engine.physics.IntersectionDetector;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;
import engine.physics.shapes.Circle;
import engine.physics.shapes.Line;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectionTests {

    Scene scene = new Scene("test", new Vector2(0, 0));
    GameObject testObject = new GameObject(scene, "test", new Vector2(0, 0));

    @Test
    void testPointOnLine() {
        Line line = new Line(new Vector2(0, 0), new Vector2(10, 10));
        Vector2 pointOnLine = new Vector2(5, 5);
        Vector2 pointOffLine = new Vector2(5, 6);

        assertTrue(IntersectionDetector.pointOnLine(line, pointOnLine), "Point should be on the line.");
        assertFalse(IntersectionDetector.pointOnLine(line, pointOffLine), "Point should not be on the line.");
    }

    @Test
    void testPointInCircle() {
        Circle circle = new Circle(testObject, 5);
        Vector2 pointInside = new Vector2(3, 4); // Inside, distance is 5
        Vector2 pointOutside = new Vector2(6, 8); // Outside, distance is greater than 5

        assertTrue(IntersectionDetector.pointInCircle(circle, pointInside), "Point should be inside the circle.");
        assertFalse(IntersectionDetector.pointInCircle(circle, pointOutside), "Point should be outside the circle.");
    }

    @Test
    void testPointInAABB() {
        AABB aabb = new AABB(testObject, new Vector2(10, 10));
        Vector2 pointInside = new Vector2(5, 5);
        Vector2 pointOnBoundary = new Vector2(0, 0);
        Vector2 pointOutside = new Vector2(11, 11);

        assertTrue(IntersectionDetector.pointInAABB(aabb, pointInside), "Point should be inside the AABB.");
        assertTrue(IntersectionDetector.pointInAABB(aabb, pointOnBoundary), "Point on boundary should be considered inside the AABB.");
        assertFalse(IntersectionDetector.pointInAABB(aabb, pointOutside), "Point should be outside the AABB.");
    }

    @Test
    void testLineInCircle() {
        Circle circle = new Circle(testObject, 5);
        Line lineIntersecting = new Line(new Vector2(-5, 0), new Vector2(5, 0)); // Should intersect the circle
        Line lineOutside = new Line(new Vector2(10, 10), new Vector2(15, 15)); // Completely outside the circle
        Line lineTangent = new Line(new Vector2(-5, 5), new Vector2(5, 5)); // Tangent to the circle

        assertTrue(IntersectionDetector.lineInCircle(circle, lineIntersecting), "Line should intersect the circle.");
        assertFalse(IntersectionDetector.lineInCircle(circle, lineOutside), "Line should not intersect the circle.");
        assertTrue(IntersectionDetector.lineInCircle(circle, lineTangent), "Line should be tangent to the circle.");
    }

    @Test
    void testLineInAABB() {
        AABB aabb = new AABB(testObject, new Vector2(10, 10));

        // Case 1: Line intersects the AABB vertically
        Line lineIntersecting = new Line(new Vector2(-7, 2), new Vector2(7, 2));
        assertTrue(IntersectionDetector.lineInAABB(aabb, lineIntersecting), "Line should intersect the AABB horizontally.");

        // Case 2: Line starts inside the AABB and exits
        Line lineStartsInside = new Line(new Vector2(3, 3), new Vector2(7, 8));
        assertTrue(IntersectionDetector.lineInAABB(aabb, lineStartsInside), "Line starting inside AABB should be considered intersecting.");

        // Case 3: Line ends inside the AABB
        Line lineEndsInside = new Line(new Vector2(-5, -8), new Vector2(-1, -4));
        assertTrue(IntersectionDetector.lineInAABB(aabb, lineEndsInside), "Line ending inside AABB should be considered intersecting.");

        // Case 4: Line is fully inside the AABB
        Line lineFullyInside = new Line(new Vector2(-3, -2), new Vector2(3, 2));
        assertTrue(IntersectionDetector.lineInAABB(aabb, lineFullyInside), "Line fully inside AABB should be considered intersecting.");

        // Case 5: Line is completely outside and does not intersect
        Line lineOutside = new Line(new Vector2(3, -8), new Vector2(7, -4));
        assertFalse(IntersectionDetector.lineInAABB(aabb, lineOutside), "Line completely outside AABB should not intersect.");

        // Case 6: Line is tangent to the AABB boundary
        Line lineTangent = new Line(new Vector2(-8, -5), new Vector2(8, -5)); // Tangent to bottom edge
        assertTrue(IntersectionDetector.lineInAABB(aabb, lineTangent), "Line tangent to AABB should be considered intersecting.");

        // Case 7: Vertical line is completely outside and does not intersect
        Line lineVertical = new Line(new Vector2(10, 2), new Vector2(20, 2));
        assertFalse(IntersectionDetector.lineInAABB(aabb, lineVertical), "Vertical line completely outside AABB should not intersect.");

        //Case 8: Horizontal line intersects with AABB
        Line lineHorizontal = new Line(new Vector2(0, -10), new Vector2(0, 10));
        assertTrue(IntersectionDetector.lineInAABB(aabb, lineHorizontal));
    }

    @Test
    void testAABBinAABB() {
        AABB b1 = new AABB(new GameObject(scene, "a", new Vector2(2, 2)), new Vector2(2, 2));
        AABB b2 = new AABB(new GameObject(scene, "b", new Vector2(2, 3)), new Vector2(2, 2));

        assertTrue(IntersectionDetector.aabbInAABB(b1, b2));
    }

}
