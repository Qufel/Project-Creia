import engine.*;
import engine.audio.AudioClip;
import engine.graphics.*;
import engine.objects.*;
import engine.physics.Vector2;
import engine.physics.shapes.AABB;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private Scene root = new Scene("MainScene", new Vector2(4096, 4096));

    private PhysicsBody player = new PhysicsBody(root, "Player", new Vector2(0, 40)) {

        @Override
        public void start(Engine engine) {

            addChildren(
                    new Sprite2D(player, "Sprite", new Vector2(0, 0), new AnimatedSprite("/res/sprites/animation.png", 16, 16)),
                    new Collider(player, "Collider", new Vector2(0, 0), new AABB(new Vector2(10, 16)))
            );


            setMass(1.0);
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

    private Camera camera = new Camera(root, player, "Camera");

    private StaticBody coin = new StaticBody(root, "Coin", new Vector2(128, 20)) {

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

    private StaticBody respawnWall = new StaticBody(root, "RespawnWall", new Vector2(0, -600)) {

        @Override
        public void start(Engine engine) {
            addChildren(
                    new Collider(respawnWall, "Collider", new Vector2(0, 0), new AABB(new Vector2(Integer.MAX_VALUE / 2, 2)))
            );
            setColliding(false);
            super.start(engine);
        }

    };

    private AudioClip coinPickup = new AudioClip("/res/audio/coin-pick.wav");

    private Tileset tileset = new Tileset("/res/sprites/tileset.png", 16, 16);

    private Tilemap walkable = new Tilemap(root, "TM_Walkable", Vector2.ZERO, tileset, "src/res/tmWalkable.csv", true);

     private Tilemap foliage = new Tilemap(root, "TM_Foliage", new Vector2(8, 16), tileset, "src/res/test_Foliage.csv", false);

    public Game() {

    }

    @Override
    public void start(Engine engine) {

//        System.out.println(tilemap.getTileIdAt(5, 6));

        //region TEST Factory of Platforms TODO: Implement class ObjectFactory
        int count = 1; // Platforms count
        Vector2 startPos = new Vector2(0, 0);
        Vector2 offset = Vector2.ZERO;

        for (int i = 0; i < count; i++) {
            StaticBody platform = new StaticBody(root, "Platform_" + (i + 1), startPos.add(offset)) {

                @Override
                public void start(Engine engine) {
                    addChildren(
                            new Collider(this, "Collider", Vector2.ZERO, new AABB(new Vector2(42, 16))),
                            new Sprite2D(this, "Sprite", Vector2.ZERO, new Sprite("/res/sprites/platform.png"))
                    );
                    setMass(0.0f);
                }

            };
            root.addChildren(platform);
        }
        //endregion

        // Run start for all objects in scene
        for(GameObject object : root.getChildren()) {
            object.start(engine);
        }

        // Setup CollisionSystem & PhysicsSystem
        root.addColliders(engine);
        root.addPhysicsBodies(engine);

    }

    @Override
    public void update(Engine engine, float delta) {

        if (engine.getInput().isKeyDown(KeyEvent.VK_NUMPAD5))
            System.out.println("Manual stop");

        //region TEST Respawn

        if (player.getCollider().isCollidingWith(respawnWall.getCollider())) {
            player.setPosition(new Vector2(0, 40));
        }

        //endregion

        // Run update for all objects in scene
        for(GameObject object : root.getChildren()) {
            if (object instanceof Camera) continue;
            object.update(engine, delta);
        }

        // Update camera after all other objects have been updated
        camera.update(engine, delta);
    }

    @Override
    public void render(Engine engine, Renderer renderer, float delta) {
        root.renderScene(engine, renderer, delta, false);

        renderer.drawText("FPS: " + engine.getFramesPerSecond() , 4 + renderer.getCamera().x, 4+ renderer.getCamera().y, 0xffffffff);
        renderer.drawText("Velocity: " + player.getVelocity(), 4 + renderer.getCamera().x, engine.getHeight() - 20 + renderer.getCamera().y, 0xffffffff);
        renderer.drawText("Acceleration: " + player.getAcceleration(), 4 + renderer.getCamera().x, engine.getHeight() - 10 + renderer.getCamera().y, 0xffffffff);
  }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setTitle("Creia");

        engine.setHeight(200);
        engine.setWidth(300);

        engine.setScale(4f);
        
        engine.start();

    }

}
