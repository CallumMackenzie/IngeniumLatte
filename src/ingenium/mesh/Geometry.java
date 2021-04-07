package ingenium.mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Geometry {
    private static ArrayList<FloatBuffer> cachedGeometry = new ArrayList<FloatBuffer>();

    public static ArrayList<FloatBuffer> getCachedGeometry() {
        return cachedGeometry;
    }

    public static void addGeometryToCache(FloatBuffer g) {
        cachedGeometry.add(g);
    }

    public static FloatBuffer getCachedGeometry(int index) {
        return cachedGeometry.get(index);
    }
}
