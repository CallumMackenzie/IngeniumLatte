package ingenium.math;

public final class Functions {
    public static double sinPulse(double time, double frequency) {
        return (0.5 * (1 + Math.sin(2 * frequency * 3.14159 * time)));
    }

    public static double cosPulse(double time, double frequency) {
        return (0.5 * (1 + Math.cos(2 * frequency * 3.14159 * time)));
    }
}
