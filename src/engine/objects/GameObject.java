package engine.objects;

import engine.Vector2;
import engine.graphics.AnimatedSprite;
import engine.graphics.Sprite;

public class GameObject {

    public Vector2 position;
    public double angle = 0.0;
    public Sprite sprite = null;
    public AnimatedSprite animatedSprite = null;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;

        if (sprite != null) {
            this.position = this.position.subtract(new Vector2(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2));
        }
    }

    public void setSprite(AnimatedSprite sprite) {
        this.animatedSprite = sprite;

        if (animatedSprite != null) {
            this.position = this.position.subtract(new Vector2(this.animatedSprite.getWidth() / 2, this.animatedSprite.getHeight() / 2));
        }
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public AnimatedSprite getAnimatedSprite() {
        return this.animatedSprite;
    }

}
