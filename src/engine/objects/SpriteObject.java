package engine.objects;

import engine.Renderer;
import engine.graphics.AnimatedSprite;
import engine.graphics.Sprite;
import engine.physics.Vector2;

public class SpriteObject extends GameObject {

    private Sprite sprite; // Sprite or AnimatedSprite
    private int z = 0;

    //region Getters & Setters

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int value) {
        this.z = value;
    }

    //endregion

    public SpriteObject(GameObject parent, String name, Sprite sprite) {
        super(parent, name == null ? "Sprite" : name , new Vector2(0, 0));

        this.sprite = sprite;
    }

    public void draw(Renderer renderer, float delta) {

        if (sprite == null) {
            return;
        }

        renderer.setZDepth(z);

        if (sprite instanceof AnimatedSprite) {

            AnimatedSprite animatedSprite = (AnimatedSprite) sprite;
            renderer.drawAnimatedSprite(animatedSprite, this.getGlobalPosition().x - animatedSprite.getWidth() / 2, this.getGlobalPosition().y - animatedSprite.getHeight() / 2, delta);

        } else {

            renderer.drawSprite(sprite, this.getGlobalPosition().x - sprite.getWidth() / 2, this.getGlobalPosition().y - sprite.getHeight() / 2);

        }

        renderer.setZDepth(0);
    }

}