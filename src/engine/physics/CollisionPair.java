package engine.physics;

import engine.objects.Collider;

public class CollisionPair {

    private Collider a;
    private Collider b;

    public CollisionPair(Collider a, Collider b) {
        this.a = a;
        this.b = b;
    }

    public Collider getA() {
        return a;
    }

    public Collider getB() {
        return b;
    }

    public static boolean compare(CollisionPair pair1, CollisionPair pair2) {
        return (pair1.a == pair2.a && pair1.b == pair2.b) || (pair1.a == pair2.b && pair1.b == pair2.a);
    }

}
