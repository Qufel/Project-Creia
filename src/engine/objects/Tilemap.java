package engine.objects;

import engine.Engine;
import engine.graphics.Tileset;
import engine.physics.Vector2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Tilemap extends StaticBody {

    //TODO: Load layout [x]
    //TODO: Generating sprite/s [ ]
    //TODO: Generating colliders [ ]

    private final int MAX_TILES = 16;

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

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
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


    }
}
