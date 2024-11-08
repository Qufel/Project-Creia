package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Sprite {

    private int width, height;
    private int[] pixels;
    private boolean alpha = false;

    public boolean flipedH = false;
    public boolean flipedV = false;

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

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public Sprite(String filename) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(Sprite.class.getResourceAsStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        width = image.getWidth();
        height = image.getHeight();

        pixels = image.getRGB(0, 0, width, height, null, 0, width);

        image.flush();
    }

    public Sprite (int[] pixels, int width, int height, boolean flipedH, boolean flipedV) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.flipedH = flipedH;
        this.flipedV = flipedV;
    }

    public Sprite() {

    }

    public boolean flipHorizontal() {
        flipedH = !flipedH;
        return flipedH;
    }

    public boolean flipVertical() {
        flipedV = !flipedV;
        return flipedV;
    }

}
