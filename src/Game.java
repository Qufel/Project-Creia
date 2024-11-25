import engine.*;
import engine.graphics.*;
import engine.objects.*;
import engine.physics.Vector2;
import engine.physics.forces.ForceGenerator;
import engine.physics.shapes.AABB;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root;

    private PhysicsBody player;
    private PhysicsBody platform1;
    private PhysicsBody platform2;
    private PhysicsBody platform3;

    private PhysicsBody wall1;
    private PhysicsBody wall2;

    public Game() {

    }

    @Override
    public void start(Engine engine) {
        root = new Scene("MainScene", new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));

        // Player setup
        player = new PhysicsBody(root, "Player", new Vector2(-64, 0));
        player.addChildren(
                new SpriteObject(player, "Sprite", new Vector2(0, 0), new AnimatedSprite("/res/sprites/animation.png", 16, 16)),
                new Collider(player, "Collider", new Vector2(0, 0), new AABB(new Vector2(10, 16)))
        );

        player.setMass(1.0);

        // Platform setup
        platform1 = new PhysicsBody(root, "Platform1", new Vector2(-64, -40));
        platform1.addChildren(
                new SpriteObject(platform1, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform1, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)))
        );

        platform1.setMass(0.0);

        // Platform setup
        platform2 = new PhysicsBody(root, "Platform2", new Vector2(0, 0));
        platform2.addChildren(
                new SpriteObject(platform2, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform2, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)))
        );

        platform2.setMass(0.0);


        // Platform setup
        platform3 = new PhysicsBody(root, "Platform3", new Vector2(-64, 40));
        platform3.addChildren(
                new SpriteObject(platform3, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform3, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)))
        );

        platform3.setMass(0.0);

//        wall1 = new PhysicsBody(root, "Wall", new Vector2(-83, 0));
//        wall1.addChildren(
//                new Collider(wall1, "Collider", new Vector2(0, 0), new AABB(new Vector2(10, 64)))
//        );
//
//        wall1.setMass(0.0);
//
//        wall2 = new PhysicsBody(root, "Wall", new Vector2(-45, 0));
//        wall2.addChildren(
//                new Collider(wall2, "Collider", new Vector2(0, 0), new AABB(new Vector2(10, 64)))
//        );
//
//        wall2.setMass(0.0);

        // Setup CollisionSystem & PhysicsSystem
        root.addColliders(engine);
        root.addPhysicsBodies(engine);

    }

    @Override
    public void update(Engine engine, float delta) {

        ((AnimatedSprite) ((SpriteObject) player.getChild("Sprite")).getSprite()).setFramesDuration(200);
        ((AnimatedSprite) ((SpriteObject) player.getChild("Sprite")).getSprite()).play();

        Vector2 direction = new Vector2(0, 0);

        if (engine.getInput().isKey(KeyEvent.VK_D)) {
            direction.x = 1;
        } else if (engine.getInput().isKey(KeyEvent.VK_A)) {
            direction.x = -1;
        }

        if (engine.getInput().isKey(KeyEvent.VK_W)) {
            direction.y = 1;
        } else if (engine.getInput().isKey(KeyEvent.VK_S)) {
            direction.y = -1;
        }

        player.setPosition(player.getPosition().add(direction.mul(2)));

//        if (engine.getInput().isKeyDown(KeyEvent.VK_W)) {
//            engine.getPhysics().getForceRegistry().add(player, new ForceGenerator() {
//                @Override
//                public void updateForce(PhysicsBody body, float delta) {
//                    body.addForce(Vector2.UP.mul(100).mul(delta * 10));
////                    engine.getPhysics().getForceRegistry().remove(body, this);
//                }
//            });
//        }

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD1)) {
            System.out.println("===");
            for (GameObject object : player.getCollider().getCollidingObjects()) {
                System.out.println("- " + object);
            }
        }

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
