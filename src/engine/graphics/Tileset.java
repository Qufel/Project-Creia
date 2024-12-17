package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class Tileset {

    private int cellWidth, cellHeight;

    private int[][] tiles;

    //region Getters & Setters

    public int[][] getTiles() {
        return tiles;
    }

    public int[] getTile(int id) {
        return tiles[id];
    }

    public void loadTileset(String filename) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Sprite.class.getResourceAsStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Initialize new tiles[][] array

        int columns = image.getWidth() / cellWidth;
        int rows = image.getHeight() / cellHeight;

        tiles = new int[columns * rows][cellWidth * cellHeight];

        // Get pixel array from original image
        int[] pixels = image.getRGB(0, 0, columns * cellWidth, rows * cellHeight, null, 0, columns * cellWidth);

        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {

                int id = c + r * columns;

                // Get offset from initial index
                int offset = r * cellHeight * (columns * cellWidth) + c * cellWidth + 1;

                // Set tile's pixels
                for (int y = 0; y < cellWidth; y++) {
                    for (int x = 0; x < cellHeight; x++) {

                        tiles[id][y * cellWidth + x] = pixels[offset + x + y * (columns * cellWidth) - 1];

                    }
                }

            }
        }

        // Flush original image
        image.flush();
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    //endregion

    public Tileset (String filename, int cWidth, int cHeight) {

        this.cellWidth = cWidth;
        this.cellHeight = cHeight;
        loadTileset(filename);
    }

}
