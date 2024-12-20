package engine.objects;

import engine.Engine;
import engine.graphics.Sprite;
import engine.graphics.Tileset;
import engine.physics.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Tilemap extends StaticBody {

    //TODO: Generating sprite/s [ ]
    //TODO: Generating colliders [ ]

    private final int MAX_TILES = 8;

    private Tileset tileset;

    private int width, height;

    private int[][] tiles;

    private int[][] collisionMap;

    private boolean generateColliders = false;

    //region Getters & Setters

    public int[][] getCollisionMap() {
        return collisionMap;
    }

    public int getCollisionAt(int x, int y) {
        return collisionMap[x][y];
    }

    public int[][] getTiles() {
        return tiles;
    }

    public int getTileIdAt(int x, int y) {
        return tiles[x][y];
    }

    public Tileset getTileset() {
        return tileset;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    //endregion

    public Tilemap(GameObject parent, String name, Vector2 position, Tileset tileset, String filename, boolean generateColliders) {
        super(parent, name, position);

        this.tileset = tileset;
        this.generateColliders = generateColliders;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            ArrayList<Integer> ids = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                // Get width and height of a tilemap
                this.width = tokens.length;
                this.height++;

                for (String token : tokens) {
                    ids.add(Integer.parseInt(token));
                }

            }

            // Initialize tiles[x][y]
            tiles = new int[this.width][this.height];

            // Initialize collisionMap[x][y]
            collisionMap = new int[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int id = ids.get(y * width + x); // Get tile id at (x, y)

                    // Assign id value at (x, y) position in tiles array.
                    // For collision map, based on the previously mentioned condition assign either 0 or 1 to indicate if the tile at (x, y) should participate in collider generation or not.
                    if (id == -1) {
                        tiles[x][y] = -1;
                        if (generateColliders)
                            collisionMap[x][y] = 0;
                    } else {
                        tiles[x][y] = id;
                        if (generateColliders)
                            collisionMap[x][y] = 1;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("File " + filename + " not found!",e);
        }

    }

    @Override
    public void start(Engine engine) {
        createSprites();
    }

    private void createSprites() {
        // Get count of how many sprites to generate
        int countX = Math.floorDiv(this.width, MAX_TILES);
        int countY = Math.floorDiv(this.height, MAX_TILES);

        if (this.width - countX * MAX_TILES> 0)
            countX++;

        if (this.height - countY * MAX_TILES> 0)
            countY++;

        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {

                // Determine width and height of each cluster (sprite)
                int width = MAX_TILES;
                int height = MAX_TILES;

                if (width * (x + 1) > this.width)
                    width = this.width - MAX_TILES * x;

                if (height * (y + 1) > this.height)
                    height = this.height - MAX_TILES * y;

                // Create cluster (sprite)
                int[] pixels = createPixelArray(x, y, width, height);
                Sprite sprite = new Sprite(pixels, tileset.getCellWidth() * width, tileset.getCellHeight() * height, false, false);

                Vector2 position = Vector2.ZERO;

                // Determine position of cluster
                Vector2 clusterCenter = new Vector2(
                        x * (MAX_TILES * tileset.getCellWidth()) + width * tileset.getCellWidth() / 2,
                        y * (MAX_TILES * tileset.getCellHeight()) + height * tileset.getCellHeight() / 2
                );

                Vector2 corner = new Vector2(
                        (-getWidth() * MAX_TILES) / 2,
                        (getHeight() * MAX_TILES) / 2
                );

                position = clusterCenter.sub(corner);

                this.addChildren(
                        new Sprite2D(this, "Sprite_" + x + "_" + y, position, sprite)
                );

            }
        }

    }

    private int[] createPixelArray(int xCoord, int yCoord, int width, int height) {
        int cellW = tileset.getCellWidth(), cellH = tileset.getCellHeight();
        int[] pixels = new int[(cellW * width) * (cellH * height)];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                // Get offset values for each tile
                int offsetX = xCoord * MAX_TILES;
                int offsetY = yCoord * MAX_TILES;

                // Get tile id
                int tile = getTileIdAt(j + offsetX, i + offsetY);

                int[] tilePixels = new int[cellW * cellH];

                // Get tile pixels
                if (tile == -1) {
                    Arrays.fill(tilePixels, 0x00000000);
                } else {
                    tilePixels = tileset.getTile(tile);
                }

                for (int y = 0; y < cellH; y++) {
                    for (int x = 0; x < cellW; x++) {

                        int index = indexAt(x, y, i, j, width);
                        pixels[index] = tilePixels[y * cellW + x];

                    }
                }

            }
        }


        return pixels;
    }

    private int indexAt(int x, int y, int i, int j, int w) {
        int cellW = tileset.getCellWidth(), cellH = tileset.getCellHeight();
        return (i * cellH + y) * (w * cellW) + (j * cellW + x);

//        return y * ((cellW * w - ((w - 1) * cellW))) + i * cellW + (y * cellW + x) + j * cellH * (w * cellW);
    }
}
