package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class AnimatedSprite extends Sprite {

    private int[][] animationFrames; // Frames of an animation
    private int[] framesDuration; // In ms

    private int currentFrame = 0;
    public double animationProgress = 0.0;

    private boolean isPlaying = false;
    public boolean loop = false;

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

        setFramesDuration(100);
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
        if (!isPlaying) {
            isPlaying = true;
        }
    }

    public void pause() {
        if (isPlaying) {
            isPlaying = false;
        }
    }

    double animationTime = 0.0;

    public void updateProgress(double amount) {
        animationProgress += amount;

        // loop or stop animation
        if (animationTime + animationProgress >= IntStream.of(framesDuration).sum() / 100.0) {
            if (loop) {

                animationProgress = 0.0;
                animationTime = 0.0;
                currentFrame = 0;

                return;
            }
            pause();
            currentFrame = 0;
            animationProgress = 0.0;
            animationTime = 0.0;
            return;
        }

        //increment current frame
        //TODO: Change to only increment after frame duration
        if (animationProgress >= framesDuration[currentFrame] / 100.0) {
            animationTime += animationProgress;
            animationProgress = 0.0;
            currentFrame++;
        }
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
