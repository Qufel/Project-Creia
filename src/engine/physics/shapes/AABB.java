package engine.physics.shapes;

import engine.objects.Collider;
import engine.objects.GameObject;
import engine.physics.Vector2;

public class AABB {

    private Collider collider;
    private Vector2 size = new Vector2(0,0);

    public AABB() {
        super();
    }

    public AABB(Vector2 size) {
        super();
        this.size = size;
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
        return new Vector2(this.getCenter()).sub(new Vector2(size).mul(0.5));
    }

    public Vector2 getMax() {
        return new Vector2(this.getCenter()).add(new Vector2(size).mul(0.5));
    }

}
