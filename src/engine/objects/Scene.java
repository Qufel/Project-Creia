package engine.objects;

import engine.Engine;
import engine.Renderer;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

public class Scene extends GameObject {

    public Scene(String name, Vector2 position) {
        super(null, name, position);
    }

    /// TODO: Scene functionality
    /// - Object hierarchy
    /// - Object rendering

    //region Physics & Collision management

    public void addPhysicsBodies(Engine engine) {

        for (GameObject child : this.getChildren()) {
            addChildrenToPhysics(child, engine);
        }

    }

    private void addChildrenToPhysics(GameObject object, Engine engine) {

        if (object.getChildren().isEmpty()) {
            if (object instanceof PhysicsBody) {
                engine.getPhysics().addBody((PhysicsBody) object);
            }
        } else {

            if (object instanceof PhysicsBody) {
                engine.getPhysics().addBody((PhysicsBody) object);
            }

            for (GameObject child : object.getChildren()) {
                addChildrenToPhysics(child, engine);
            }
        }

    }

    public void addColliders(Engine engine) {
        for (GameObject child : this.getChildren()) {
            addChildrenToCollision(child, engine);
        }
    }

    private void addChildrenToCollision(GameObject object, Engine engine) {

        if (object.getChildren().isEmpty()) {
            if (object instanceof Collider) {
                engine.getCollision().addCollider((Collider) object);
            }
        } else {

            if (object instanceof Collider) {
                engine.getCollision().addCollider((Collider) object);
            }

            for (GameObject child : object.getChildren()) {
                addChildrenToCollision(child, engine);
            }
        }

    }

    //endregion


    public void renderScene(Engine engine, Renderer renderer, float delta, boolean debugColliders) {

        for(GameObject child : this.getChildren()) {
            renderChildren(child, engine, renderer, delta, debugColliders);
        }

    }

    private void renderChildren(GameObject object, Engine engine, Renderer renderer, float delta, boolean debugColliders) {

        if (object.getChildren().isEmpty()) {

            // Render self
            renderObject(object, renderer, delta, debugColliders);

        } else {

            // Render self and then continue for children

            renderObject(object, renderer, delta, debugColliders);

            for (GameObject child : object.getChildren()) {
                renderChildren(child, engine, renderer, delta, debugColliders);
            }
        }

    }

    private void renderObject(GameObject object, Renderer renderer, float delta, boolean debugColliders) {
        // Check if object should be visible if not don't render

        if (!object.isVisible())
            return;

        // Draw object if drawable

        if (object instanceof SpriteObject) {
            SpriteObject sprite = (SpriteObject) object;
            sprite.draw(renderer, delta);
        }

        //region Debug

        if (debugColliders) {
            if (object instanceof Collider) {

                AABB aabb = ((Collider) object).getAABB();
                Vector2 center = new Vector2(aabb.getCenter()).sub(new Vector2(aabb.getSize()).mul(0.5));

                int color = 0xff00ff00;
                if (((Collider) object).isColliding())
                    color = 0xffff0000;

                renderer.drawRect(center.x, center.y, aabb.getSize().x - 1, aabb.getSize().y - 1, color);
            }
        }

        //endregion

    }

}
