package engine;

import engine.physics.CollisionSystem;
import engine.physics.PhysicsSystem;
import engine.physics.Vector2;

public class Engine implements Runnable {

    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private PhysicsSystem physics;
    private CollisionSystem collision;

    private AbstractEngine aEngine;

    private boolean running = false;

    public final boolean UNCAPPED_FPS = false;
    private final double FRAMES_CAP = 60.0;
    private double updateCap = 1.0/FRAMES_CAP;

    private int framesPerSecond;

    //region Display

    private int width = 240, height = 180;
    private float scale = 1f;
    private String title = "2D Game Project";

    //endregion

    public Engine(AbstractEngine aEngine) {
        this.aEngine = aEngine;
    }

    public void start() {

        // Engine classes
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        collision = new CollisionSystem();
        physics = new PhysicsSystem(1f / 60f, new Vector2(0, -50));

        aEngine.start(this);

        // Sort Colliders
        collision.sortByMinX();
        System.out.println("Sorted colliders");

        thread = new Thread(this);
        thread.run();


    }

    public void stop() {

    }

    public void run() {

        running = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        if (UNCAPPED_FPS && fps > 0)
            updateCap = (double) 1 / fps;

        while (running) {

            render = UNCAPPED_FPS;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= updateCap) {
                unprocessedTime -= updateCap;
                render = true;

                // Collision update
                collision.update();

                // Physics update
                physics.update((float) updateCap);

                // Update Game
                aEngine.update(this, (float)updateCap);

                input.update(); //Should be last

                if(frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }

                framesPerSecond = fps;
            }

            if (render) {

                renderer.clear();

                // Render Game
                aEngine.render(this, renderer, (float)updateCap);
                renderer.process();
                renderer.drawText("FPS: " + fps , 4, 4, 0xffffffff);

                window.update();
                frames++;

            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        dispose();

    }

    private void dispose() {

    }

    //region Getters & Setters

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public PhysicsSystem getPhysics() {
        return physics;
    }

    public CollisionSystem getCollision() {
        return collision;
    }

    public int getFramesPerSecond() {
        return this.framesPerSecond;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    //endregion

}
