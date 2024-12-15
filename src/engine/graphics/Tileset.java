package engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    //endregion

    public Tileset (String filename, int cWidth, int cHeight) {

        this.cellWidth = cWidth;
        this.cellHeight = cHeight;

        BufferedImage image = null;

        try {
            image = ImageIO.read(Sprite.class.getResourceAsStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int columns = image.getWidth() / cWidth;
        int rows = image.getHeight() / cHeight;

        System.out.println(columns + " x " + rows);

        tiles = new int[columns * rows][cWidth * cHeight];

        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {

                int id = c + r * columns;

                int[] pixels = image.getRGB(0, 0, columns * cWidth, rows * cHeight, null, 0, columns * cWidth);

                int start = r * cHeight * (columns * cWidth) + c * cWidth + 1;

                for (int y = 0; y < cWidth; y++) {
                    for (int x = 0; x < cHeight; x++) {

                        int pixel = pixels[start + x + y * (columns * cWidth) - 1];

                        tiles[id][y * cWidth + x] = pixel;

                    }
                }

            }
        }

        image.flush();

    }

}
