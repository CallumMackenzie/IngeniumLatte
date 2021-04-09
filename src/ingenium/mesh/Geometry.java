package ingenium.mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;

import ingenium.Utils;
import ingenium.math.Vec2;
import ingenium.math.Vec3;

public class Geometry {
    private static final Cache<String, ValueCacheElement> valueCache = new Cache<>("geometry cache", false); // 1
    private static final Cache<String, Integer[]> referenceCache = new Cache<>("geometry cache", false); // 2

    public static class Object3D {
        private int VAO;
        private int VBO;
        private boolean reference;
        private FloatBuffer data;
        private int numVerts;

        public Object3D(int VBO, int VAO, int numVerts) {
            this.numVerts = numVerts;
            this.VBO = VBO;
            this.VAO = VAO;
            this.reference = true;
        }

        public Object3D(FloatBuffer data, int numVerts) {
            this.data = data;
            this.numVerts = numVerts;
            this.reference = false;
        }

        public boolean isReference() {
            return reference;
        }

        public FloatBuffer getData() {
            return data;
        }

        public int getNumVerts() {
            return numVerts;
        }

        public int getVAO() {
            return VAO;
        }

        public int getVBO() {
            return VBO;
        }
    }

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

    public static Cache<String, Integer[]> getReferenceCache() {
        return referenceCache;
    }

    public static Cache<String, ValueCacheElement> getValueCache() {
        return valueCache;
    }

    /**
     * 
     * @param raw  the path or raw obj data
     * @param path whether the passed value is a path or raw data
     */
    public static Object3D loadFromObjData(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        boolean cacheDebug = false;
        boolean debugLoading = false;
        String objPath = "string obj data" + raw.hashCode();
        if (path)
            objPath = raw;
        long time = System.currentTimeMillis();
        if (Geometry.getValueCache().isUsed() && useGeometryValueCache)
            if (Geometry.getValueCache().containsKey(objPath)) {
                Geometry.ValueCacheElement elem = Geometry.getValueCache().getCacheValue(objPath);
                Object3D object3d = new Object3D(elem.getBuffer(), elem.getNumVerts());
                if (cacheDebug)
                    System.out.println(
                            "Value cache hit (" + objPath + "): " + (System.currentTimeMillis() - time) + "ms");
                return object3d;
            }
        if (Geometry.getReferenceCache().isUsed() && useGeometryReferenceCache)
            if (Geometry.getReferenceCache().containsKey(objPath)) {
                if (cacheDebug)
                    System.out.println(
                            "Reference cache hit (" + objPath + "): " + (System.currentTimeMillis() - time) + "ms");
                Integer[] ref = Geometry.getReferenceCache().getCacheValue(objPath);
                return new Object3D(ref[0], ref[1], ref[2]);
            }
        if (path)
            raw = Utils.getFileAsString(raw);
        ArrayList<Tri> tris = new ArrayList<Tri>();
        ArrayList<Vec3> verts = new ArrayList<Vec3>();
        ArrayList<Vec3> normals = new ArrayList<Vec3>();
        ArrayList<Vec2> texs = new ArrayList<Vec2>();
        String lines[] = raw.split("\n");

        boolean hasNormals = raw.contains("vn");
        boolean hasTexture = raw.contains("vt");
        if (cacheDebug)
            System.out.println("Loading .obj with " + lines.length + " lines...");

        for (int i = 0; i < lines.length; i++) {
            if (debugLoading)
                System.out.println(((float) i / (float) lines.length) + "% loading (" + i + " / " + lines.length + ")");
            String line = lines[i];
            if (line.charAt(0) == 'v') {
                if (line.charAt(1) == 't') {
                    String seg[] = line.split(" ");
                    texs.add(new Vec2(Float.parseFloat(seg[1]), Float.parseFloat(seg[2])));
                } else if (line.charAt(1) == 'n') {
                    String seg[] = line.split(" ");
                    normals.add(new Vec3(Float.parseFloat(seg[1]), Float.parseFloat(seg[2]), Float.parseFloat(seg[3])));
                } else {
                    String seg[] = line.split(" ");
                    verts.add(new Vec3(Float.parseFloat(seg[1]), Float.parseFloat(seg[2]), Float.parseFloat(seg[3])));
                }
            } else if (line.charAt(0) == 'f') {
                int params = 1;
                if (hasNormals)
                    params++;
                if (hasTexture)
                    params++;

                ArrayList<Integer> vals = new ArrayList<Integer>();
                String seg[] = line.replace("f", "").split("[\\/\\s]+");

                for (int l = 1; l < seg.length; l++)
                    vals.add(Integer.parseInt(seg[l]));

                Tri push = new Tri();
                for (int k = 0; k < 3; k++) {
                    Tri.Vert v = new Tri.Vert(verts.get(vals.get(params * k) - 1));
                    if (hasTexture)
                        v.setT(texs.get(vals.get((params * k) + 1) - 1));
                    if (hasNormals && !hasTexture)
                        v.setN(normals.get(vals.get((params * k) + 1) - 1));
                    if (hasNormals && hasTexture)
                        v.setN(normals.get(vals.get((params * k) + 2) - 1));
                    push.setVert(k, v);
                }

                tris.add(push);
            }
        }
        verts.clear();
        normals.clear();
        texs.clear();
        int numVerts = 0;
        float[] dataToWrite = new float[tris.size() * Tri.Vert.vertSize * 3];
        for (int l = 0; l < tris.size(); l++) {
            if (debugLoading)
                System.out.println(
                        ((float) l / (float) tris.size() * 100f) + "% formatting (" + l + " / " + tris.size() + ")");
            numVerts += 3;
            System.arraycopy(tris.get(l).toDataArray(), 0, dataToWrite, l * Tri.Vert.floatVertSize,
                    Tri.Vert.floatVertSize);
        }
        FloatBuffer data = Buffers.newDirectFloatBuffer(dataToWrite);
        Geometry.getValueCache().add(objPath, new Geometry.ValueCacheElement(data, numVerts));
        if (cacheDebug)
            System.out.println("No cache hits (" + objPath + "): " + (System.currentTimeMillis() - time) + "ms");
        return new Object3D(data, numVerts);
    }
}
