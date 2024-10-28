import engine.*;
import engine.graphics.AnimatedSprite;
import engine.graphics.Font;
import engine.objects.PhysicsBody;

import java.awt.event.KeyEvent;

public class Game extends AbstractEngine {

    private PhysicsBody testBody;

    public Game() {

    }

    // Runs before the main thread starts
    @Override
    public void start(Engine engine) {
        testBody = new PhysicsBody(new Vector2(engine.getWidth() / 2, engine.getHeight() / 2));
        testBody.setSprite(new AnimatedSprite("/res/sprites/animation.png", 16, 16));

        Font font = new Font("/res/fonts/NotJamFont.png");
    }

    // Runs every update frame
    @Override
    public void update(Engine engine, float delta) {

        if(engine.getInput().isKeyDown(KeyEvent.VK_H)) {
            testBody.getAnimatedSprite().flipHorizontal();
        }

        if(engine.getInput().isKeyDown(KeyEvent.VK_V)) {
            testBody.getAnimatedSprite().flipVertical();
        }

        if(engine.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            testBody.getAnimatedSprite().pause();
        }

        if(engine.getInput().isKeyDown(KeyEvent.VK_1)) {
            testBody.getAnimatedSprite().play();
        }

    }

    // Runs every render frame
    @Override
    public void render(Engine engine, Renderer renderer, float delta) {
        renderer.drawAnimatedSprite(testBody.getAnimatedSprite(), testBody.position.x, testBody.position.y, 0);
    }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setScale(4f);
        
        engine.start();

    }

}
