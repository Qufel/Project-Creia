import engine.*;
import engine.audio.AudioClip;
import engine.graphics.*;
import engine.objects.*;
import engine.physics.Vector2;
import engine.physics.forces.ForceGenerator;
import engine.physics.shapes.AABB;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root;

    private PhysicsBody player;
    private StaticBody platform1;
    private StaticBody platform2;
    private StaticBody platform3;

    private StaticBody coin;

    private StaticBody respawnWall;

    AudioClip coinPickup = new AudioClip("/res/audio/coin-pick.wav");

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
        ((AnimatedSprite) ((SpriteObject) player.getChild("Sprite")).getSprite()).setFramesDuration(200);

//        ( (SpriteObject) player.getChild("Sprite")).setRenderingLayer(RenderingLayer.PLAYER.ordinal());

        // Platform setup
        platform1 = new StaticBody(root, "Platform1", new Vector2(-64, -40));
        platform1.addChildren(
                new SpriteObject(platform1, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform1, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)))
        );
        platform2 = new StaticBody(root, "Platform2", new Vector2(0, 0));
        platform2.addChildren(
                new SpriteObject(platform2, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform2, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)))
        );
        platform3 = new StaticBody(root, "Platform3", new Vector2(-64, 40));
        platform3.addChildren(
                new SpriteObject(platform3, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/platform.png")),
                new Collider(platform3, "Collider", new Vector2(0, 0), new AABB(new Vector2(48, 16)))
        );

        respawnWall = new StaticBody(root, "RespawnWall", new Vector2(0, -200));
        respawnWall.addChildren(
                new Collider(respawnWall, "Collider", new Vector2(0, 0), new AABB(new Vector2(Integer.MAX_VALUE / 2, 2)))
        );

        respawnWall.setColliding(false);

        coin = new StaticBody(root, "Coin", new Vector2(0, 20));
        coin.addChildren(
                new SpriteObject(coin, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/coin.png")),
                new Collider(coin, "Collider", new Vector2(0, 0), new AABB(new Vector2(12, 12)))
        );

        coin.setColliding(false);
//        player.setFreeze(true);
        coinPickup.setVolume(-15);

        // Setup CollisionSystem & PhysicsSystem
        root.addColliders(engine);
        root.addPhysicsBodies(engine);

    }

    @Override
    public void update(Engine engine, float delta) {

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD5))
            System.out.println("Manual stop");

        ((AnimatedSprite) ((SpriteObject) player.getChild("Sprite")).getSprite()).play();

        //region TEST Movement

        Vector2 direction = new Vector2(0, 0);
        Vector2 playerVelocity = player.getVelocity();

        if (engine.getInput().isKey(KeyEvent.VK_D)) {
            direction.x = 1;
        } else if (engine.getInput().isKey(KeyEvent.VK_A)) {
            direction.x = -1;
        }

        if (engine.getInput().isKeyDown(KeyEvent.VK_W)) {
            player.addForce(Vector2.UP.mul(800).mul(delta * 10));
        }

        player.setVelocity(new Vector2((int) (direction.x * 10), playerVelocity.y));

        //endregion

        //region TEST Coin pickup

        if (coin != null) {
            if (player.getCollider().isCollidingWith(coin.getCollider())) {
                if (!coinPickup.isPlaying()) {
                    coinPickup.play();
                }
                coin.decompose();
                coin = null;
            }
        }

        //endregion

        //region TEST Respawn

        if (player.getCollider().isCollidingWith(respawnWall.getCollider())) {
            player.setPosition(new Vector2(-64, 0));
            player.setVelocity(new Vector2(0, 0));
            player.clearForce();
        }

        //endregion

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD1)) {
            System.out.println("===");
            for (GameObject object : player.getCollider().getCollidingObjects()) {
                System.out.println("- " + object);
            }
        }

    }

    @Override
    public void render(Engine engine, Renderer renderer, float delta) {
        root.renderScene(engine, renderer, delta, false);

        renderer.drawText(player.getGlobalPosition().toString(), engine.getWidth() - 50, 4, 0xffffffff);

        renderer.drawText("Velocity: " + player.getVelocity(), 4, engine.getHeight() - 20, 0xffffffff);
        renderer.drawText("Acceleration: " + player.getAcceleration(), 4, engine.getHeight() - 10, 0xffffffff);
  }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setScale(4f);
        
        engine.start();

    }

}
