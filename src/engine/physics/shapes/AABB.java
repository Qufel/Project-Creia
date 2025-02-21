package engine.physics.shapes;

import engine.objects.Collider;
import engine.objects.GameObject;
import engine.physics.Vector2;

public class AABB {

    private Collider collider;
    private Vector2 size = Vector2.ZERO;
    private Vector2 centerDisplacement = Vector2.ZERO;

    public AABB() {

    }

    public AABB(AABB aabb) {
        this.collider = aabb.collider;
        this.size = aabb.size;
        this.centerDisplacement = aabb.centerDisplacement;
    }

    public AABB(Vector2 size) {
        this.size = size;
    }

    public AABB(Vector2 size, Vector2 centerDisplacement) {
        this.size = size;
        this.centerDisplacement = centerDisplacement;
    }

    public AABB(Collider collider, Vector2 size) {
        this.collider = collider;
        this.size = size;
    }

    public Vector2 getCenter() {
        return collider.getGlobalPosition();
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getMin() {
        return new Vector2(this.getCenter().add(centerDisplacement)).sub(new Vector2(size).mul(0.5));
    }

    public Vector2 getMax() {
        return new Vector2(this.getCenter().add(centerDisplacement)).add(new Vector2(size).mul(0.5));
    }

    public AABB move(Vector2 displacement) {
        return new AABB(this.size, displacement);
    }
}
