package engine.objects;

import engine.Engine;
import engine.Renderer;
import engine.physics.Vector2;

public class Scene extends GameObject {

    public Scene(String name, Vector2 position) {
        super(null, name, position);
    }

    /// TODO: Scene functionality
    /// - Object hierarchy
    /// - Object rendering

    public void renderScene(Engine engine, Renderer renderer, float delta) {

        for(GameObject child : this.getChildren()) {
            renderChildren(child, engine, renderer, delta);
        }

    }

    private void renderChildren(GameObject object, Engine engine, Renderer renderer, float delta) {

        if (object.getChildren().isEmpty()) {

            // Render self
            renderObject(object, renderer, delta);

        } else {

            // Render self and then continue for children

            renderObject(object, renderer, delta);

            for (GameObject child : object.getChildren()) {
                renderChildren(child, engine, renderer, delta);
            }
        }

    }

    private void renderObject(GameObject object, Renderer renderer, float delta) {
        // Check if object should be visible if not don't render

        if (!object.isVisible())
            return;

        // Draw object if drawable

        if (object instanceof SpriteObject) {
            SpriteObject sprite = (SpriteObject) object;
            sprite.draw(renderer, delta);
        }


    }

}
