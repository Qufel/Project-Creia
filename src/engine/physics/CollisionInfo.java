package engine.physics;

import engine.objects.Collider;

public class CollisionInfo {

    private Collider a;
    private Collider b;

    private int collisionNormals = 0b0000;
    // Binary data where each bit signs if there is collision (1) on direction from a to b or there isn't (0)
    // Directions are as follows UP, RIGHT, DOWN, LEFT

    public CollisionInfo(Collider a, Collider b) {
        this.a = a;
        this.b = b;
    }

    public Collider getA() {
        return a;
    }

    public Collider getB() {
        return b;
    }

    public void setCollisionNormals(int collisionNormals) {
        this.collisionNormals = collisionNormals;
    }

    public int getCollisionNormals() {
        return collisionNormals;
    }

    public static boolean compare(CollisionInfo pair1, CollisionInfo pair2) {
        return (pair1.a == pair2.a && pair1.b == pair2.b) || (pair1.a == pair2.b && pair1.b == pair2.a);
    }

}
