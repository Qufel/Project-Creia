import engine.*;
import engine.graphics.*;
import engine.objects.*;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root;

    private PhysicsBody player;
    private StaticBody platform1;
    private StaticBody platform2;
    private StaticBody platform3;

    public Game() {

    }

    @Override
    public void start(Engine engine) {
        root = new Scene("MainScene", new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));

        // Player setup
        player = new PhysicsBody(root, "Player", new Vector2(-64, 32), engine.getPhysics());
        player.addChildren(
                new SpriteObject(player, "Sprite", new Vector2(0, 0), new AnimatedSprite("/res/sprites/animation.png", 16, 16)),
                new Collider(player, "Collider", new Vector2(0, 0), new AABB(new Vector2(16, 16)), engine.getCollision())
        );

        player.setMass(1.0);

        // Platform setup
        platform1 = new StaticBody(root, "Platform1", new Vector2(-64, -40));
        platform1.addChildren(
                new SpriteObject(platform1, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform1, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)), engine.getCollision())
        );

        // Platform setup
        platform2 = new StaticBody(root, "Platform2", new Vector2(0, 0));
        platform2.addChildren(
                new SpriteObject(platform2, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform2, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)), engine.getCollision())
        );

        // Platform setup
        platform3 = new StaticBody(root, "Platform3", new Vector2(64, 40));
        platform3.addChildren(
                new SpriteObject(platform3, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform3, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)), engine.getCollision())
        );
    }

    @Override
    public void update(Engine engine, float delta) {

        ((AnimatedSprite) ((SpriteObject) player.getChild("Sprite")).getSprite()).setFramesDuration(200);
        ((AnimatedSprite) ((SpriteObject) player.getChild("Sprite")).getSprite()).play();

        int direction = 0;

        if (engine.getInput().isKey(KeyEvent.VK_D)) {
            direction = 1;
        } else if (engine.getInput().isKey(KeyEvent.VK_A)) {
            direction = -1;
        }

        player.setPosition(player.getPosition().add(Vector2.RIGHT.mul(direction).mul(2)));

        if (engine.getInput().isKeyDown(KeyEvent.VK_W)) {
            if (player.isOnGround()) {
                player.addForce(Vector2.UP.mul(200));
            }
        }

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
        root.renderScene(engine, renderer, delta, true);

        renderer.drawText("On Ground: " + player.isOnGround(), 4, engine.getHeight() - 30, 0xffffffff);
        renderer.drawText("Velocity: " + player.getVelocity(), 4, engine.getHeight() - 20, 0xffffffff);
        renderer.drawText("Acceleration: " + player.getAcceleration(), 4, engine.getHeight() - 10, 0xffffffff);
  }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setScale(4f);
        
        engine.start();

    }

}
