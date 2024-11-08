package engine.objects;

import engine.physics.Vector2;

public class StaticBody extends GameObject {

    public StaticBody(GameObject parent, String name, Vector2 position) {
        super(parent, name, position);
    }

}
