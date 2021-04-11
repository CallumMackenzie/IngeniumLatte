package ingenium.mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import ingenium.utilities.*;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.mesh.Triangle.Tri2D;
import ingenium.mesh.Triangle.Tri3D;
import ingenium.utilities.Cache;

public class Geometry {
    private static final Cache<String, ValueCacheElement> valueCache = new Cache<>("geometry cache", false); // 1
    private static final Cache<String, Integer[]> referenceCache = new Cache<>("geometry cache", false); // 2

    public static class Object {
        private int VAO;
        private int VBO;
        private boolean reference;
        private FloatBuffer data;
        private int numVerts;

        public Object(int VBO, int VAO, int numVerts) {
            this.numVerts = numVerts;
            this.VBO = VBO;
            this.VAO = VAO;
            this.reference = true;
        }

        public Object(FloatBuffer data, int numVerts) {
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

    private static ArrayList<Tri2D> loadTri2DArrayFromObj(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        ArrayList<Tri3D> tri3ds = loadTri3DArrayFromObj(raw, path, useGeometryReferenceCache, useGeometryValueCache);
        ArrayList<Tri2D> tri2ds = new ArrayList<>();
        for (Tri3D t : tri3ds)
            tri2ds.add(t.toTri2D());
        return tri2ds;
    }

    private static ArrayList<Tri3D> loadTri3DArrayFromObj(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        if (path)
            raw = FileUtils.getFileAsString(raw);
        ArrayList<Tri3D> tris = new ArrayList<Tri3D>();
        ArrayList<Vec3> verts = new ArrayList<Vec3>();
        ArrayList<Vec3> normals = new ArrayList<Vec3>();
        ArrayList<Vec2> texs = new ArrayList<Vec2>();
        String lines[] = raw.split("\n");

        boolean hasNormals = raw.contains("vn");
        boolean hasTexture = raw.contains("vt");

        for (int i = 0; i < lines.length; i++) {
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

                Tri3D push = new Tri3D();
                for (int k = 0; k < 3; k++) {
                    Tri3D.Vert v = new Tri3D.Vert(verts.get(vals.get(params * k) - 1));
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
        return tris;
    }

    private static Geometry.Object checkGeometryCaches(String name, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        // Checking value cache
        if (Geometry.getValueCache().isUsed() && useGeometryValueCache)
            if (Geometry.getValueCache().containsKey(name)) {
                Geometry.ValueCacheElement elem = Geometry.getValueCache().getCacheValue(name);
                return new Geometry.Object(elem.getBuffer(), elem.getNumVerts());
            }
        // Checking reference cache
        if (Geometry.getReferenceCache().isUsed() && useGeometryReferenceCache)
            if (Geometry.getReferenceCache().containsKey(name)) {
                Integer[] ref = Geometry.getReferenceCache().getCacheValue(name);
                return new Geometry.Object(ref[0], ref[1], ref[2]);
            }
        return null;
    }

    public static Geometry.Object loadFromObjData(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        String objPath = "string obj data" + raw.hashCode();
        if (path)
            objPath = raw;
        // Check if we've loaded this before
        Geometry.Object cacheObject = checkGeometryCaches(objPath, useGeometryReferenceCache, useGeometryValueCache);
        if (cacheObject != null) // We have!
            return cacheObject; // Return what was found
        // Parse .obj data
        ArrayList<Tri3D> tris = loadTri3DArrayFromObj(raw, path, useGeometryReferenceCache, useGeometryValueCache);
        int numVerts = tris.size() * 3;
        // Move data to a FloatBuffer
        float[] dataToWrite = new float[tris.size() * Tri3D.Vert.floatVertSize];
        for (int l = 0; l < tris.size(); l++)
            System.arraycopy(tris.get(l).toDataArray(), 0, dataToWrite, l * Tri3D.Vert.floatVertSize,
                    Tri3D.Vert.floatVertSize);
        FloatBuffer data = Buffers.newDirectFloatBuffer(dataToWrite);
        // Add value cache data
        Geometry.getValueCache().add(objPath, new Geometry.ValueCacheElement(data, numVerts));
        return new Geometry.Object(data, numVerts);
    }

    public static Geometry.Object loadFromObjData2D(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        String objPath = "string obj data" + raw.hashCode();
        if (path)
            objPath = raw;
        // Check if we've loaded this before
        Geometry.Object cacheObject = checkGeometryCaches(objPath, useGeometryReferenceCache, useGeometryValueCache);
        if (cacheObject != null) // We have!
            return cacheObject; // Return what was found
        // Parse .obj data
        ArrayList<Tri2D> tris = loadTri2DArrayFromObj(raw, path, useGeometryReferenceCache, useGeometryValueCache);
        int numVerts = tris.size() * 3;
        // Move data to a FloatBuffer
        float[] dataToWrite = new float[tris.size() * Tri2D.Vert.floatVertSize];
        for (int l = 0; l < tris.size(); l++)
            System.arraycopy(tris.get(l).toDataArray(), 0, dataToWrite, l * Tri2D.Vert.floatVertSize,
                    Tri2D.Vert.floatVertSize);
        FloatBuffer data = Buffers.newDirectFloatBuffer(dataToWrite);
        // Add value cache data
        Geometry.getValueCache().add(objPath, new Geometry.ValueCacheElement(data, numVerts));
        return new Geometry.Object(data, numVerts);
    }
}
