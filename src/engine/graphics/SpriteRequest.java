package engine.graphics;

public class SpriteRequest {

    public Sprite sprite;
    public int zDepth;
    public int offX, offY;

    public SpriteRequest(Sprite sprite, int zDepth, int offX, int offY) {
        this.sprite = sprite;
        this.zDepth = zDepth;
        this.offX = offX;
        this.offY = offY;
    }

}
