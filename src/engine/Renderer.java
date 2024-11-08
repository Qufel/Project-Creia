package engine;

import engine.graphics.AnimatedSprite;
import engine.graphics.Font;
import engine.graphics.Sprite;
import engine.graphics.SpriteRequest;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer {

    private int pixelW, pixelH;
    private int[] pixels;
    private int[] zBuffer;

    private int zDepth = 0;
    private boolean processing = false;

    private Font font = Font.STANDARD;
    private ArrayList<SpriteRequest> spriteRequests = new ArrayList<SpriteRequest>();

    private final int BACKGROUND_COLOR = 0xff8db7ff;

    public Renderer(Engine engine) {

        pixelH = engine.getHeight();
        pixelW = engine.getWidth();

        // Allows for pixel manipulation in the image
        pixels = ((DataBufferInt)engine.getWindow().getImage().getRaster().getDataBuffer()).getData();

        zBuffer = new int[pixels.length];
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = BACKGROUND_COLOR;
            zBuffer[i] = 0;
        }
    }

    public void process() {

        processing = true;

        // Sort Sprite Requests by zDepth
        Collections.sort(spriteRequests, new Comparator<SpriteRequest>() {

            @Override
            public int compare(SpriteRequest o1, SpriteRequest o2) {
                if (o1.zDepth < o2.zDepth) {
                    return -1;
                } else if (o1.zDepth > o2.zDepth) {
                    return 1;
                }
                return 0;
            }

        });

        // Render alpha
        for (SpriteRequest request : spriteRequests) {
            setZDepth(request.zDepth);
            drawSprite(request.sprite, request.offX, request.offY);
        }
        spriteRequests.clear();

        processing = false;
    }

    public void setPixel(int x, int y, int color) {

        int alpha = ((color >> 24) & 0xff);

        if (((x < 0 || x >= pixelW) || (y < 0 || y >= pixelH)) || alpha == 0) {
            return;
        }

        int index = x + y * pixelW;

        if (zBuffer[index] > zDepth) {
            return;
        }

        zBuffer[index] = zDepth;

        if (alpha == 255) {
            pixels[y * pixelW + x] = color;
        } else {

            int source = pixels[index];

            int red = ((source >> 16) & 0xff) + (int)((((source >> 16) & 0xff) - ((color >> 16) & 0xff)) * (alpha / 255f));
            int green = ((source >> 8) & 0xff) + (int)((((source >> 8) & 0xff) - ((color >> 8) & 0xff)) * (alpha / 255f));
            int blue = (source & 0xff) + (int)(((source & 0xff) - (color & 0xff)) * (alpha / 255f));

            int output = (255 << 24) | (red << 16) | (green << 8) | blue;

            pixels[index] = output;
        }

    }

    public void drawSprite(Sprite sprite, int offX, int offY) {

        if (sprite.isAlpha() && !processing) {
            spriteRequests.add(new SpriteRequest(sprite, zDepth, offX, offY));
            return;
        }

        int newX = 0, newY = 0;
        int newWidth = sprite.getWidth();
        int newHeight = sprite.getHeight();

        //Skip rendering because the sprite is out of bounds
        if (offX < -newWidth) { return; }
        if (offY < -newHeight) { return; }
        if (offX >= pixelW) { return; }
        if (offY >= pixelH) { return; }

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

    public void drawAnimatedSprite(AnimatedSprite sprite, int offX, int offY, float delta) {

        if (!sprite.isPlaying()) {

            //TODO: rendering n-th set frame
            Sprite newSprite = new Sprite(sprite.getFrames()[sprite.getCurrentFrame()], sprite.getWidth(), sprite.getHeight(), sprite.flipedH, sprite.flipedV);
            newSprite.setAlpha(sprite.isAlpha());

            drawSprite(newSprite, offX, offY);

        } else {

            //TODO: rendering animation frames
            sprite.updateProgress(sprite.getAnimationSpeed() * delta);

            Sprite newSprite = new Sprite(sprite.getFrames()[sprite.getCurrentFrame()], sprite.getWidth(), sprite.getHeight(), sprite.flipedH, sprite.flipedV);
            newSprite.setAlpha(sprite.isAlpha());

            drawSprite(newSprite, offX, offY);

        }

    }

    public void drawText(String text, int offX, int offY, int color) {
        int offset = 0;

        for (int i = 0; i < text.length(); i++) {

            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < font.getFontImage().getHeight(); y++)  {
                for (int x = 0; x < font.getWidths()[unicode]; x++)  {
                    if (font.getFontImage().getPixels()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }

                }
            }

            offset += font.getWidths()[unicode];

        }

    }

    //region Primitives

    public void drawRect(int offX, int offY, int width, int height, int color) {

        for (int y = 0; y <= height; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }
        for (int x = 0; x <= width; x++) {
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }

    }

    public void fillRect(int offX, int offY, int width, int height, int color) {

        //Skip rendering because the sprite is out of bounds
        if (offX < -width) { return; }
        if (offY < -height) { return; }
        if (offX >= pixelW) { return; }
        if (offY >= pixelH) { return; }

        int newX = 0, newY = 0;
        int newWidth = width;
        int newHeight = height;

        //Clipping parts of sprite that is out of screen
        if (offX < 0) { newX -= offX; }
        if (offY < 0) { newY -= offY; }

        if (newWidth + offX > pixelW) {
            newWidth -= (newWidth + offX - pixelW); }
        if (newHeight + offY > pixelH) {
            newHeight -= (newHeight + offY - pixelH); }

        for (int y = newY; y <= newHeight; y++) {
            for (int x = newX; x <= newWidth; x++) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    //endregion

    public int getZDepth() {
        return zDepth;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
