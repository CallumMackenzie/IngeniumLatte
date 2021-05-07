package ingenium.math;

public final class Mathematics {
    public static double sinPulse(double time, double frequency) {
        return (0.5 * (1 + Math.sin(2 * frequency * 3.14159 * time)));
    }

    public static double cosPulse(double time, double frequency) {
        return (0.5 * (1 + Math.cos(2 * frequency * 3.14159 * time)));
    }

    public static double lerp (double a, double b, double t) {
        return (a * (1.0 - t)) + (b * t);
    }
}
