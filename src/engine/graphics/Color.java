package engine.graphics;

import java.util.concurrent.ThreadLocalRandom;

public class Color {
    
    public static final int RED = 0xffff0000;
    public static final int GREEN = 0xff00ff00;
    public static final int BLUE = 0xff0000ff;
    public static final int BLACK = 0xff000000;
    public static final int WHITE = 0xffffffff;
    public static final int GRAY = 0xff808080;
    public static final int CYAN = 0xff00ffff;
    public static final int MAGENTA = 0xffff00ff;
    public static final int YELLOW = 0xffffff00;

    public static final int randomize() {
        return ThreadLocalRandom.current().nextInt(0x1000000) | 0xff000000;
    }
}
