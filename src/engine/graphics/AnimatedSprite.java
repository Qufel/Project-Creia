package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class AnimatedSprite extends Sprite {

    private int[][] animationFrames; // Frames of an animation
    private int[] framesDuration; // In ms

    private int currentFrame = 0;
    private double animationProgress = 0.0;

    private boolean isPlaying = false;
    private boolean loop = false;

    public AnimatedSprite(String filename, int tileWidth, int tileHeight) {

        super(filename);

        this.setWidth(tileWidth);
        this.setHeight(tileHeight);

        if (filename == null) { throw new NullPointerException("Filename is null"); }

        BufferedImage image = null;

        try {
            image = ImageIO.read(AnimatedSprite.class.getResourceAsStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setWidth(tileWidth);
        this.setHeight(tileHeight);

        int tilesCount = image.getWidth() / tileWidth; // Tiles (Frames) count in image

        animationFrames = new int[tilesCount][tileWidth * tileHeight];
        framesDuration = new int[tilesCount];

        for (int i = 0; i < tilesCount; i++) {
            for (int y = 0; y < tileHeight; y++) {
                for (int x = 0; x < tileWidth; x++) {
                    animationFrames[i][x + y * tileWidth] = image.getRGB(x + (i * tileWidth), y);
                }
            }
        }

    }

    //region Frame Duration

    public AnimatedSprite setFramesDuration(int ...durations) {
        if (framesDuration.length == 0) { throw new IllegalArgumentException("Animated sprite has no frames"); }
        framesDuration = durations.clone();
        return this;
    }

    public AnimatedSprite setFramesDuration(int duration) {
        if (framesDuration.length == 0) { throw new IllegalArgumentException("Animated sprite has no frames"); }
        Arrays.fill(framesDuration, duration);
        return this;
    }

    //endregion

    //region Animation Controller

    public void play() {
        isPlaying = true;
    }

    public void pause() {
        isPlaying = false;
    }

    public void updateProgress(double amount) {
        animationProgress += amount;
        if (animationProgress >= animationFrames.length) {
            animationProgress = 0.0;
        }
        currentFrame = (int)animationProgress;
    }

    //endregion

    public int[][] getFrames() {
        return animationFrames;
    }

    public int[] getFramesDuration() {
        return framesDuration;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public AnimatedSprite setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        return this;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

}
