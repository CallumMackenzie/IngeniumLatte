package ingenium.mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.jogamp.common.nio.Buffers;
import ingenium.utilities.*;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.mesh.Triangle.Tri;
import ingenium.mesh.Triangle.Tri2D;
import ingenium.mesh.Triangle.Tri3D;
import ingenium.utilities.Cache;

public class Geometry {
    public static final String QUAD_PATH = "QUAD";
    private static final Cache<String, ValueCacheElement> valueCache = new Cache<>("geometry cache", false); // 1
    private static final Cache<String, Integer[]> referenceCache = new Cache<>("geometry cache", false); // 2

    public static abstract class CustomLoader<TRI_TYPE> {
        protected boolean checkValueCache = true;
        protected boolean checkReferenceCache = true;
        protected boolean useDefaultLoad = true;

        public ArrayList<TRI_TYPE> onPostLoad(ArrayList<TRI_TYPE> preloaded) {
            // TODO: custom object loading class
            return null;
        };

        public ArrayList<TRI_TYPE> onTryLoad(String raw, boolean path, boolean useGeometryReferenceCache,
                boolean useGeometryValueCache) {
            return null;
        }
    }

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

    public static <T> Geometry.Object executeCustomLoader(CustomLoader<T> loader, String raw, boolean path,
            boolean useRefCache, boolean useValCache) {
        // TODO: custom object loading
        if (loader.useDefaultLoad) {
            loadFromObjData(raw, path, useRefCache, useValCache);
        }
        return null;
    }

    public static <T> ArrayList<T> loadTriArrayFromObj(String raw, boolean path, boolean useRefCache,
            boolean useValCache) {
        return null;
    }

    public static ArrayList<Tri2D> loadTri2DArrayFromObj(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        ArrayList<Tri3D> tri3ds = loadTri3DArrayFromObj(raw, path, useGeometryReferenceCache, useGeometryValueCache);
        ArrayList<Tri2D> tri2ds = new ArrayList<>();
        for (Tri3D t : tri3ds)
            tri2ds.add(t.toTri2D());
        tri3ds = null;
        HashMap<String, Tri2D> tris = new HashMap<>();
        for (Tri2D t : tri2ds)
            if (!(t.getVert(0).getP().equalsXY(t.getVert(1).getP()) // v0 == v1
                    || t.getVert(0).getP().equalsXY(t.getVert(2).getP()) // v0 == v2
                    || t.getVert(1).getP().equalsXY(t.getVert(2).getP()))) // v1 == v2
                tris.put(Integer.toString(t.hashCode()), t);
        tri2ds = new ArrayList<>(tris.values());
        return tri2ds;
    }

    public static ArrayList<Tri3D> loadTri3DArrayFromObj(String raw, boolean path, boolean useGeometryReferenceCache,
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

    public static Geometry.Object make2DQuad() {
        Tri2D.Vert topLeft = new Tri2D.Vert(new Vec2(-1, 1), new Vec2(0, 1));
        Tri2D.Vert topRight = new Tri2D.Vert(new Vec2(1, 1), new Vec2(1, 1));
        Tri2D.Vert bottomLeft = new Tri2D.Vert(new Vec2(-1, -1), new Vec2(0, 0));
        Tri2D.Vert bottomRight = new Tri2D.Vert(new Vec2(1, -1), new Vec2(1, 0));
        ArrayList<Tri2D> tris = new ArrayList<>(
                Arrays.asList(new Tri2D[] { new Tri2D(new Tri2D.Vert[] { topLeft, bottomLeft, topRight }),
                        new Tri2D(new Tri2D.Vert[] { bottomLeft, bottomRight, topRight }) }));
        return geometryArrayListToObject(tris);
    }

    public static Geometry.Object loadFromObjData(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        String objPath = "string obj data" + raw.hashCode();
        if (path)
            objPath = raw;
        Geometry.Object cacheObject = checkGeometryCaches(objPath, useGeometryReferenceCache, useGeometryValueCache);
        if (cacheObject != null)
            return cacheObject;
        ArrayList<Tri3D> tris = loadTri3DArrayFromObj(raw, path, useGeometryReferenceCache, useGeometryValueCache);
        Geometry.Object gObject = geometryArrayListToObject(tris);
        Geometry.getValueCache().add(objPath, new Geometry.ValueCacheElement(gObject.data, gObject.numVerts));
        return gObject;
    }

    public static Geometry.Object loadFromObjData2D(String raw, boolean path, boolean useGeometryReferenceCache,
            boolean useGeometryValueCache) {
        String objPath = "string obj data" + raw.hashCode();
        if (path)
            objPath = raw;
        Geometry.Object cacheObject = checkGeometryCaches(objPath, useGeometryReferenceCache, useGeometryValueCache);
        if (cacheObject != null)
            return cacheObject;
        ArrayList<Tri2D> tris = loadTri2DArrayFromObj(raw, path, useGeometryReferenceCache, useGeometryValueCache);
        Geometry.Object gObject = geometryArrayListToObject(tris);
        Geometry.getValueCache().add(objPath, new Geometry.ValueCacheElement(gObject.data, gObject.numVerts));
        return gObject;
    }

    public static <T> Geometry.Object geometryArrayListToObject(ArrayList<T> list) {
        if (list.size() < 0)
            return new Geometry.Object(null, 0);
        if (list.get(0) instanceof Tri2D) {
            int numVerts = list.size() * 3;
            float[] dataToWrite = new float[list.size() * Tri2D.Vert.vertSize * 3];
            for (int l = 0; l < list.size(); l++)
                System.arraycopy(((Tri2D) list.get(l)).toDataArray(), 0, dataToWrite, l * Tri2D.Vert.vertSize * 3,
                        Tri2D.Vert.vertSize * 3);
            return new Geometry.Object(Buffers.newDirectFloatBuffer(dataToWrite), numVerts);
        } else if (list.get(0) instanceof Tri3D) {
            int numVerts = list.size() * 3;
            float[] dataToWrite = new float[list.size() * Tri3D.Vert.vertSize * 3];
            for (int l = 0; l < list.size(); l++)
                System.arraycopy(((Tri3D) list.get(l)).toDataArray(), 0, dataToWrite, l * Tri3D.Vert.vertSize * 3,
                        Tri3D.Vert.vertSize * 3);
            return new Geometry.Object(Buffers.newDirectFloatBuffer(dataToWrite), numVerts);
        }
        return new Geometry.Object(null, 0);
    }
}
