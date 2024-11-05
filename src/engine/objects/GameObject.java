package engine.objects;

import engine.Vector2;
import engine.graphics.Sprite;

public class GameObject {

    private GameObject parent;
    private GameObject[] children;

    public Vector2 position;

    private Sprite sprite = null;

    //region Sprites

    public Sprite setSprite(Sprite sprite) {
        this.sprite = sprite;

        if (sprite != null) {
            this.position = this.position.subtract(new Vector2(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2));
        }

        return sprite;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    //endregion

}
