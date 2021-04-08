package ingenium.mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Geometry {
    public static class ValueCacheElement {
        private FloatBuffer buffer;
        private int numVerts;

        public ValueCacheElement(FloatBuffer buffer, int numVerts) {
            this.numVerts = numVerts;
            this.buffer = buffer;
        }

        public int getNumVerts() {
            return numVerts;
        }

        public FloatBuffer getBuffer() {
            return buffer;
        }
    }

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
