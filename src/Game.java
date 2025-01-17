import engine.*;
import engine.audio.AudioClip;
import engine.graphics.*;
import engine.objects.*;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Game extends AbstractEngine {

    private ArrayList<Scene> scenes = new ArrayList<>(Arrays.asList(
            new Scene("MainScene", new Vector2(4096, 4096)) {
                private PhysicsBody player = new PhysicsBody(this, "Player", new Vector2(0, 40)) {

                    @Override
                    public void start(Engine engine) {

                        setRestitution(0.0f);

                        addChildren(
                                new Sprite2D(player, "Sprite", new Vector2(0, 0), new AnimatedSprite("/res/sprites/animation.png", 16, 16)),
                                new Collider(player, "Collider", new Vector2(0, 0), new AABB(new Vector2(10, 16)))
                        );


                        setMass(1.0f);
                        ((AnimatedSprite) ((Sprite2D) getChild("Sprite")).getSprite()).setFramesDuration(200);
                        ((Sprite2D) getChild("Sprite")).setRenderingLayer(RenderingLayer.PLAYER.ordinal());
                        super.start(engine);
                    }

                    @Override
                    public void update(Engine engine, float delta) {
                        ((AnimatedSprite) ((Sprite2D) this.getChild("Sprite")).getSprite()).play();

                        Vector2 direction = new Vector2(0, 0);
                        Vector2 playerVelocity = this.getVelocity();

                        if (engine.getInput().isKey(KeyEvent.VK_D)) {
                            direction.x = 1;
                        } else if (engine.getInput().isKey(KeyEvent.VK_A)) {
                            direction.x = -1;
                        }

                        if (engine.getInput().isKeyDown(KeyEvent.VK_W)) {
                            this.addForce(Vector2.UP.mul(800).mul(delta * 10));
                        }

                        this.setVelocity(new Vector2(direction.x * 10, playerVelocity.y));

                        super.update(engine, delta);
                    }
                };

                private Camera camera = new Camera(this, player, "Camera");

                private StaticBody coin = new StaticBody(this, "Coin", new Vector2(128, 20)) {

                    @Override
                    public void start(Engine engine) {

                        this.addChildren(
                                new Sprite2D(coin, "Sprite", new Vector2(0, 0), new Sprite("/res/sprites/coin.png")),
                                new Collider(coin, "Collider", new Vector2(0, 0), new AABB(new Vector2(12, 12)))
                        );

                        setColliding(false);
                        coinPickup.setVolume(-15);

                    }

                    @Override
                    public void update(Engine engine, float delta) {
                        if (coin != null) {
                            if (player.getCollider().isCollidingWith(this.getCollider())) {
                                if (!coinPickup.isPlaying()) {
                                    coinPickup.play();
                                }
                                this.decompose();
                                coin = null;
                            }
                        }

                        super.update(engine, delta);
                    }
                };;

                private StaticBody respawnWall = new StaticBody(this, "RespawnWall", new Vector2(0, -600)) {

                    @Override
                    public void start(Engine engine) {
                        addChildren(
                                new Collider(respawnWall, "Collider", new Vector2(0, 0), new AABB(new Vector2(2000, 2)))
                        );
                        setColliding(false);
                        super.start(engine);
                    }

                    @Override
                    public void update(Engine engine, float delta) {
                        if (player.getCollider().isCollidingWith(respawnWall.getCollider())) {
                            player.setPosition(new Vector2(0, 40));
                        }
                    }

                };

                private AudioClip coinPickup = new AudioClip("/res/audio/coin-pick.wav");

                private Tileset tileset = new Tileset("/res/sprites/tileset.png", 16, 16);

                private Tilemap walkable = new Tilemap(this, "TM_Walkable", Vector2.ZERO, tileset, "/res/tmWalkable.csv", true);

                private Tilemap foliage = new Tilemap(this, "TM_Foliage", new Vector2(8, 16), tileset, "/res/test_Foliage.csv", false);

                @Override
                public void start(Engine engine) {
                    super.start(engine);
                }

                @Override
                public void update(Engine engine, float delta) {
                    super.update(engine, delta);
                }

            }
    ));

    public Game() {

    }

    @Override
    public void start(Engine engine) {

        // Run start for all scenes
        for(Scene scene : scenes) {
            scene.start(engine);
        }

    }

    @Override
    public void update(Engine engine, float delta) {

        engine.getCollision().clearColliders();
        engine.getPhysics().clearBodies();


        for (Scene scene : scenes) {

            // Setup CollisionSystem & PhysicsSystem
            scene.addColliders(engine);
            scene.addPhysicsBodies(engine);

            scene.update(engine, delta);
        }

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD5))
            System.out.println("Manual stop");

    }

    @Override
    public void render(Engine engine, Renderer renderer, float delta) {

        for (Scene scene : scenes) {
            scene.renderScene(engine, renderer, delta, true);
        }

        renderer.drawText("FPS: " + engine.getFramesPerSecond() , 4 + renderer.getCamera().x, 4+ renderer.getCamera().y, 0xffffffff);
        renderer.drawText("Velocity: " + ((PhysicsBody)scenes.get(0).getChild("Player")).getVelocity(), 4 + renderer.getCamera().x, engine.getHeight() - 20 + renderer.getCamera().y, 0xffffffff);
        renderer.drawText("Acceleration: " + ((PhysicsBody)scenes.get(0).getChild("Player")).getAcceleration(), 4 + renderer.getCamera().x, engine.getHeight() - 10 + renderer.getCamera().y, 0xffffffff);
  }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setTitle("Creia");

        engine.setHeight(200);
        engine.setWidth(300);

        engine.setScale(4f);

        engine.setFullscreen(false);

        engine.start();

    }

}
