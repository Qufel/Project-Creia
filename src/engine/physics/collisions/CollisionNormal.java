package engine.physics.collisions;

import engine.objects.Collider;
import engine.physics.*;

public class CollisionNormal {

    private Collider collider;
    private Vector2 normal;

    public CollisionNormal(Collider collider, Vector2 normal) {
        this.collider = collider;
        this.normal = normal;
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof CollisionNormal n)) return false;

        if (n.getCollider() == this.getCollider() && n.getNormal() == this.getNormal()) {
            return true;
        }

        return false;
    }
}
