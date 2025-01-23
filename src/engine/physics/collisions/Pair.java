package engine.physics.collisions;

import engine.objects.Collider;

public class Pair {
    public Collider a;
    public Collider b;

    public Pair(Collider a, Collider b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Pair pair)) return false;

        return (pair.a == this.a && pair.b == this.b) || (pair.a == this.b && pair.b == this.a);
    }

    @Override
    public String toString() {
        return "( " + a.toString() + ", " + b.toString() + " )";
    }
}