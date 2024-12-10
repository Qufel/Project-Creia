package engine;

public class EMath {

    public static int ceilFloor(float value) {
        if (value < 0) {
            return (int) Math.floor(value);
        } else {
            return (int) Math.ceil(value);
        }
    }

    public static int ceilFloor(double value) {
        if (value < 0) {
            return (int) Math.floor(value);
        } else {
            return (int) Math.ceil(value);
        }
    }

}
