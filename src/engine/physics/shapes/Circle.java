package engine.physics.shapes;

import engine.objects.GameObject;

public class Circle extends CollisionShape {

    private int radius = 4;

    public Circle() {}

    public Circle(GameObject body, int radius) {
        super(body);
        this.radius = radius;
    }

}
