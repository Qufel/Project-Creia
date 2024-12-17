package engine.objects;

import engine.Engine;
import engine.Renderer;
import engine.graphics.AnimatedSprite;
import engine.graphics.RenderingLayer;
import engine.graphics.Sprite;
import engine.physics.Vector2;

public class Sprite2D extends GameObject {

    private Sprite sprite; // Sprite or AnimatedSprite
    private int renderingLayer = RenderingLayer.BACKGROUND.ordinal();

    //region Getters & Setters

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getRenderingLayer() {
        return renderingLayer;
    }

    public void setRenderingLayer(int value) {
        this.renderingLayer = value;
    }

    //endregion

    public Sprite2D(GameObject parent, String name, Vector2 position, Sprite sprite) {
        super(parent, name == null ? "Sprite" : name , position);

        this.sprite = sprite;
    }

    @Override
    public void update(Engine engine, float delta) {

    }

    public void draw(Renderer renderer, float delta) {

        if (sprite == null) {
            return;
        }

        renderer.setRenderingDepth(renderingLayer);

        if (sprite instanceof AnimatedSprite) {

            AnimatedSprite animatedSprite = (AnimatedSprite) sprite;
            renderer.drawAnimatedSprite(animatedSprite, this.getGlobalPosition().x - animatedSprite.getWidth() / 2, this.getGlobalPosition().y - animatedSprite.getHeight() / 2, delta);

        } else {

            renderer.drawSprite(sprite, this.getGlobalPosition().x - sprite.getWidth() / 2, this.getGlobalPosition().y - sprite.getHeight() / 2);

        }

        renderer.setRenderingDepth(0);
    }

}