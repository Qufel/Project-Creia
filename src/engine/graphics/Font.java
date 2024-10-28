package engine.graphics;

import java.awt.*;

public class Font {

    public final static Font STANDARD = new Font("/res/fonts/NotJamFont.png");

    private Sprite fontImage;

    private int[] offsets;
    private int[] widths;

    public Font(String filename) {

        fontImage = new Sprite(filename);

        offsets = new int[95];
        widths = new int[95];

        int unicode = 0;

        for (int i = 0; i < fontImage.getWidth(); i++) {

            if (fontImage.getPixels()[i] == 0xff0000ff) {
                offsets[unicode] = i;
            }

            if (fontImage.getPixels()[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }

        }

    }

    public int[] getWidths() {
        return widths;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public Sprite getFontImage() {
        return fontImage;
    }

}
