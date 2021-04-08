package ingenium.mesh;

import java.nio.FloatBuffer;

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

}
