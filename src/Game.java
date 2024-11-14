import engine.*;
import engine.graphics.*;
import engine.objects.*;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root;

    private GameObject b1;
    private GameObject b2;
    private GameObject b3;

    public Game() {

    }

    @Override
    public void start(Engine engine) {
        root = new Scene("MainScene", new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));

        b1 = new GameObject(root, "Box_1",new Vector2(0, 0));
        b2 = new GameObject(root, "Box_2",new Vector2(64, 0));
        b3 = new GameObject(root, "Box_3",new Vector2(-64, 0));

        // Setup Sprites

        b1.addChildren(new SpriteObject(
                b1,
                "Sprite",
                new Vector2(0,0),
                Primitives.Circle(8, 0xff00ff00)
        ));

        b2.addChildren(new SpriteObject(
                b2,
                "Sprite",
                new Vector2(0,0),
                Primitives.Rect(32, 20, 0xff0000ff)
        ));

        b3.addChildren(new SpriteObject(
                b3,
                "Sprite",
                new Vector2(0, 0),
                Primitives.Rect(16, 8, 0xffab02cf)
        ));

        ((SpriteObject) b1.getChild("Sprite")).setRenderingLayer(RenderingLayer.PLAYER.ordinal());

        // Setup Colliders

        b1.addChildren(new Collider(
                b1,
                "Collider",
                new Vector2(0, 0),
                new AABB(new Vector2(16, 16)),
                engine.getCollision()
        ));

        b2.addChildren(new Collider(
                b2,
                "Collider",
                new Vector2(0, 0),
                new AABB(new Vector2(32, 20)),
                engine.getCollision()
        ));

        b3.addChildren(new Collider(
                b3,
                "Collider",
                new Vector2(0, 0),
                new AABB(new Vector2(16, 8)),
                engine.getCollision()
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

        int color = 0xff00ff00;

        Collider collider = (Collider) b1.getChild("Collider");
        SpriteObject sprite = (SpriteObject) b1.getChild("Sprite");

        if (collider.isCollidingWith(b2.getChild("Collider")))
            color = 0xffff0000;

        if (collider.isCollidingWith(b3.getChild("Collider")))
            color = 0xff0000ff;

        sprite.setSprite(Primitives.Circle(8, color));


        //region Debug Input

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD1)) {
            System.out.println("List of PhysicsBodies:");
            for (PhysicsBody b : engine.getPhysics().getPhysicsBodies()) {
                System.out.println("- " + b);
            }
        }

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD2)) {
            System.out.println("List of Colliders:");
            for (Collider c : engine.getCollision().getColliders()) {
                System.out.println("- " + c);
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
