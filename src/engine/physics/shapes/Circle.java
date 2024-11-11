package engine.physics.shapes;

import engine.objects.GameObject;
import engine.physics.Vector2;

public class Circle extends CollisionShape {

    private int radius = 4;

    public Circle() {}

    public Circle(GameObject body, int radius) {
        super(body);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

}
