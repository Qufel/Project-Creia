package engine.objects;

import engine.physics.Vector2;

public class Camera extends GameObject {

    private GameObject reference;

    public Camera(GameObject parent, GameObject reference, String name) {
        super(parent, name, Vector2.ZERO);
        this.reference = reference;
    }
}
