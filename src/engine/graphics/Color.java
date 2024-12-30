package engine.graphics;

import java.util.concurrent.ThreadLocalRandom;

public class Color {
    
    public static final int RED = 0xff0000;
    public static final int GREEN = 0x00ff00;
    public static final int BLUE = 0x0000ff;
    public static final int BLACK = 0x000000;
    public static final int WHITE = 0xffffff;
    public static final int GRAY = 0x808080;
    public static final int CYAN = 0x00ffff;
    public static final int MAGENTA = 0xff00ff;
    public static final int YELLOW = 0xffff00;

    public static final int randomize() {
        return ThreadLocalRandom.current().nextInt(0x1000000) | 0xff000000;
    }
}
