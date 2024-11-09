import engine.*;
import engine.graphics.*;
import engine.objects.PhysicsBody;
import engine.objects.Scene;
import engine.objects.SpriteObject;
import engine.physics.Physics;
import engine.physics.Vector2;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root;

    private PhysicsBody player;
    private SpriteObject sprite;

    public Game() {

    }

    @Override
    public void start(Engine engine) {
        root = new Scene("MainScene", new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));

        player = new PhysicsBody(
                root,
                "Player",
                new Vector2(0, 0),
                engine.getPhysics()
        );

        sprite = new SpriteObject(
                player,
                "Sprite",
                player.getPosition(),
                new AnimatedSprite(
                        "/res/sprites/animation.png",
                        16, 16
                )
        );

        ((AnimatedSprite)sprite.getSprite()).setAnimationSpeed(4.0);


    }

    @Override
    public void update(Engine engine, float delta) {

        ((AnimatedSprite)sprite.getSprite()).play();

        //region Debug Input

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD1)) {
            System.out.println("List of PhysicsBodies:");
            for (PhysicsBody b : engine.getPhysics().getPhysicsBodies()) {
                System.out.println("- " + b);
            }
        }

        //endregion
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
