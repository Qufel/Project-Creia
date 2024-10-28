package engine;

import engine.graphics.AnimatedSprite;
import engine.graphics.Font;
import engine.graphics.Sprite;

import java.awt.*;
import java.awt.image.DataBufferInt;

public class Renderer {

    private int pixelW, pixelH;
    private int[] pixels;

    public Renderer(Engine engine) {

        pixelH = engine.getHeight();
        pixelW = engine.getWidth();

        //TODO: Allows for pixel manipulation in the image
        pixels = ((DataBufferInt)engine.getWindow().getImage().getRaster().getDataBuffer()).getData();

    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0xff000000;
        }
    }

    public void setPixel(int x, int y, int color) {
        if(((x < 0 || x >= pixelW) || (y < 0 || y >= pixelH)) || color == 0x00000000) {
            return;
        }
        pixels[y * pixelW + x] = color;
    }

    public void drawSprite(Sprite sprite, int offX, int offY) {

        int newX = 0, newY = 0;
        int newWidth = sprite.getWidth();
        int newHeight = sprite.getHeight();

        //Skip rendering because the sprite is out of bounds
        if (offX < -newWidth) { return; }
        if (offY < -newHeight) { return; }

        //Clipping parts of sprite that is out of screen
        if (offX < 0) { newX -= offX; }
        if (offY < 0) { newY -= offY; }

        if (newWidth + offX > pixelW) {
            newWidth -= (newWidth + offX - pixelW); }
        if (newHeight + offY > pixelH) {
            newHeight -= (newHeight + offY - pixelH); }

        if (sprite.flipedH) {
            if (sprite.flipedV) {
                for (int y = sprite.getHeight() - newY - 1; y >= (sprite.getHeight() - newHeight); y--) {
                    for (int x = sprite.getWidth() - newX - 1; x >= (sprite.getWidth() - newWidth); x--) {
                        setPixel(sprite.getWidth() - x + offX - 1, sprite.getHeight() - y + offY - 1, sprite.getPixels()[y * sprite.getWidth() + x]);
                    }
                }
            } else {
                for(int y = newY; y < newHeight; y++) {
                    for(int x = sprite.getWidth() - newX - 1; x >= (sprite.getWidth() - newWidth); x--) {
                        setPixel(sprite.getWidth() - x + offX - 1, y + offY, sprite.getPixels()[y * sprite.getWidth() + x]);
                    }
                }
            }
        } else if (sprite.flipedV) {
            for(int y = sprite.getHeight() - newY - 1; y >= (sprite.getHeight() - newHeight); y--) {
                for(int x = newX; x < newWidth; x++) {
                    setPixel(x + offX, sprite.getHeight() - y + offY - 1, sprite.getPixels()[y * sprite.getWidth() + x]);
                }
            }
        } else {
            for(int y = newY; y < newHeight; y++) {
                for(int x = newX; x < newWidth; x++) {
                    setPixel(x + offX, y + offY, sprite.getPixels()[y * sprite.getWidth() + x]);
                }
            }
        }
    }

    public void drawAnimatedSprite(AnimatedSprite sprite, int offX, int offY, int frame) {

        if (!sprite.isPlaying()) {

            //TODO: rendering n-th set frame
            Sprite newSprite = new Sprite(sprite.getFrames()[sprite.getCurrentFrame()], sprite.getWidth(), sprite.getHeight(), sprite.flipedH, sprite.flipedV);
            drawSprite(newSprite, offX, offY);

        } else {

            //TODO: rendering animation frames
            sprite.updateProgress(0.1);

            Sprite newSprite = new Sprite(sprite.getFrames()[sprite.getCurrentFrame()], sprite.getWidth(), sprite.getHeight(), sprite.flipedH, sprite.flipedV);
            drawSprite(newSprite, offX, offY);

        }

    }


}
