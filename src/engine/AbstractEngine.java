package engine;

public abstract class AbstractEngine {

    public abstract void start(Engine engine);
    public abstract void update(Engine engine, float delta);
    public abstract void render(Engine engine, Renderer renderer, float delta);

}
