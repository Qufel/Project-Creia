import engine.*;
import engine.graphics.*;
import engine.objects.GameObject;
import engine.objects.PhysicsBody;
import engine.objects.Scene;
import engine.objects.SpriteObject;
import engine.physics.Physics;
import engine.physics.Vector2;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root;

    private GameObject b1;
    private GameObject b2;

    public Game() {

    }

    @Override
    public void start(Engine engine) {
        root = new Scene("MainScene", new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));

        b1 = new GameObject(root, "Box_1",new Vector2(-64, 0));
        b2 = new GameObject(root, "Box_2",new Vector2(64, 0));

        b1.addChildren(new SpriteObject(
                b1,
                "Sprite",
                new Vector2(0,0),
                Primitives.Rect(16, 16, 0xff00ff00)
        ));

        b2.addChildren(new SpriteObject(
                b2,
                "Sprite",
                new Vector2(0,0),
                Primitives.Rect(32, 8, 0xff0000ff)
        ));
    }

    @Override
    public void update(Engine engine, float delta) {

        Vector2 direction = Vector2.ZERO;

        if (engine.getInput().isKey(KeyEvent.VK_A))
            direction = Vector2.LEFT;

        if (engine.getInput().isKey(KeyEvent.VK_D))
            direction = Vector2.RIGHT;

        b1.setPosition(new Vector2(b1.getPosition()).add(direction.mul(delta * 100)));

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
