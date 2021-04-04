package ingenium.math;

public class Rotation {
    public static float degToRad(float degrees) {
        return degrees * ((float) Math.PI / 180.f);
    }

    public static float radToDeg(float radians) {
        return radians * (180.f / (float) Math.PI);
    }
}
