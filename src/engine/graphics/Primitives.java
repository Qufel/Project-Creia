package engine.graphics;

public class Primitives {

    /**
     * Creates a rectangle sprite.
     * @param width width of the rectangle.
     * @param height height of the rectangle.
     * @param color color of the drawn rectangle.
     * @return Rectangle of the size <strong>width</strong> x <strong>height</strong> and set <strong>color</strong>.
     */
    public static Sprite Rect(int width, int height, int color) {

        Sprite sprite = new Sprite();

        if (((color >> 24) & 0xff) != 255)
            sprite.setAlpha(true);

        sprite.setWidth(width);
        sprite.setHeight(height);

        int[] pixels = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            pixels[i] = color;
        }

        sprite.setPixels(pixels);

        return sprite;
    }

    public static Sprite Circle (int radius, int color) {

        Sprite sprite = new Sprite();

        if (((color >> 24) & 0xff) != 255)
            sprite.setAlpha(true);

        int[] pixels = new int[4 * radius * radius];

        sprite.setHeight(2 * radius);
        sprite.setWidth(2 * radius);

        int centerX = radius, centerY = radius;

        for (int y = 0; y < radius * 2; y++) {
            for (int x = 0; x < radius * 2; x++) {

                float distance = (float) Math.sqrt(Math.pow(x - radius, 2) + Math.pow(y - radius, 2));

                if (distance < radius) {
                    pixels[y * (2 * radius) + x] = color;
                }

            }
        }

        sprite.setPixels(pixels);

        return sprite;

    }

}
