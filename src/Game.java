import engine.*;
import engine.audio.*;
import engine.graphics.*;
import engine.objects.GameObject;
import engine.objects.SceneObject;
import engine.objects.SpriteObject;
import engine.physics.Vector2;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private SceneObject root;
    private SpriteObject player;

    public Game() {

    }

    @Override
    public void start(Engine engine) {
        root = new SceneObject("MainScene", new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));

        player = new SpriteObject(
                root,
                "Player",
                new Vector2(0, 0),
                new AnimatedSprite("/res/sprites/animation.png", 16, 16));

        ((AnimatedSprite)(player.getSprite())).setAnimationSpeed(4.0);
        player.setRenderingLayer(RenderingLayer.PLAYER.ordinal());
    }

    @Override
    public void update(Engine engine, float delta) {
        ((AnimatedSprite)player.getSprite()).play();

        float speed = 100 * delta;

        int direction = 0;

        if (engine.getInput().isKey(KeyEvent.VK_D)) {
            if (player.getSprite().flipedH) {
                player.getSprite().flipHorizontal();
            }
            direction = 1;
        } else if (engine.getInput().isKey(KeyEvent.VK_A)) {
            if (!player.getSprite().flipedH) {
                player.getSprite().flipHorizontal();
            }
            direction = -1;
        }

        Vector2 newPosition = new Vector2(Math.round(player.getPosition().x + speed * direction), player.getPosition().y);
        player.setPosition(newPosition);
    }

    @Override
    public void render(Engine engine, Renderer renderer, float delta) {
        root.renderScene(engine, renderer, delta);
  }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setScale(4f);
        
        engine.start();

    }

}
