import engine.*;
import engine.graphics.*;

public class Game extends AbstractEngine {

    private int mouseX, mouseY;

    private AnimatedSprite player = new AnimatedSprite("/res/sprites/animation.png", 16, 16);
    private Sprite platform = new Sprite("/res/sprites/platform.png");

    public Game() {
        player.loop = true;
        platform.setAlpha(true);
        player.setAlpha(true);
    }

    @Override
    public void start(Engine engine) {

    }

    @Override
    public void update(Engine engine, float delta) {

        mouseX = engine.getInput().getMouseX();
        mouseY = engine.getInput().getMouseY();

        player.updateProgress(delta * 5);
    }

    @Override
    public void render(Engine engine, Renderer renderer, float delta) {
        renderer.setZDepth(2);
        renderer.drawAnimatedSprite(player, mouseX - 8, mouseY - 8, -1);
        renderer.setZDepth(0);
        renderer.drawSprite(platform, engine.getWidth() / 2 - 24, engine.getHeight() / 2 + 16);
    }

    public static void main(String[] args) {

        Engine engine = new Engine(new Game());
        engine.setScale(4f);
        
        engine.start();

    }

}
