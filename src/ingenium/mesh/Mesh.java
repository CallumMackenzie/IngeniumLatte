package ingenium.mesh;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.world.Camera;
import ingenium.world.Shader;
import ingenium.world.light.DirectionalLight;

public class Mesh {
    private Vec3 position;
    private Vec3 rotation;
    private Vec3 rotationCenter;
    private Vec3 scale;
    private Vec3 tint = new Vec3();
    private Material material;

    private boolean loaded = false;
    private FloatBuffer data;
    private int mVBO;
    private int mVAO;
    private int mTVBO;

    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationCenter, Material material) {
        this.rotation = rotation;
        this.rotationCenter = rotationCenter;
        this.position = position;
        this.scale = scale;
        this.material = material;
    }

    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationCenter) {
        this(position, rotation, rotationCenter, scale, new Material());
    }

    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale) {
        this(position, rotation, scale, new Vec3());
    }

    public Mesh(Vec3 position, Vec3 rotation) {
        this(position, rotation, new Vec3(1, 1, 1));
    }

    public Mesh(Vec3 position) {
        this(position, new Vec3());
    }

    public Mesh() {
        this(new Vec3());
    }

    public void loadFromObjData(String raw) {
        ArrayList<Tri> tris = new ArrayList<Tri>();
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
                    Vec2 v = new Vec2(Float.parseFloat(seg[1]), Float.parseFloat(seg[2]));
                    texs.add(v);
                } else if (line.charAt(1) == 'n') {
                    String seg[] = line.split(" ");
                    Vec3 normal = new Vec3(Float.parseFloat(seg[1]), Float.parseFloat(seg[2]),
                            Float.parseFloat(seg[3]));
                    normals.add(normal);
                } else {
                    String seg[] = line.split(" ");
                    Vec3 ve = new Vec3(Float.parseFloat(seg[1]), Float.parseFloat(seg[2]), Float.parseFloat(seg[3]));
                    verts.add(ve);
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
        float[] dataToWrite = new float[0];
        for (Tri t : tris) {
            float triDat[] = t.toDataArray(this);
            int datLen = dataToWrite.length;
            int triLen = triDat.length;
            float newData[] = new float[datLen + triLen];
            System.arraycopy(dataToWrite, 0, newData, 0, datLen);
            System.arraycopy(triDat, 0, newData, datLen, triLen);
        }
        data = FloatBuffer.allocate(dataToWrite.length);
        data.put(dataToWrite);
    }

    public void load(GL2 gl) {
        if (!loaded) {
            int aVBO[] = { mVBO };
            int aVAO[] = { mVAO };
            gl.glGenBuffers(1, aVBO, 0);
            mVBO = aVBO[0];
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, mVBO);
            gl.glBufferData(mVBO, data.position() * Float.BYTES, data, GL.GL_DYNAMIC_DRAW);

            gl.glGenVertexArrays(1, aVAO, 0);
            mVAO = aVAO[0];
            gl.glBindVertexArray(mVAO);
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, mVBO);

            gl.glVertexAttribPointer(0, 4, GL.GL_FLOAT, false, Tri.Vert.vertSize * Float.BYTES, 0);
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, Tri.Vert.vertSize * Float.BYTES, 4 * Float.BYTES);
            gl.glEnableVertexAttribArray(1);

            gl.glVertexAttribPointer(2, 4, GL.GL_FLOAT, false, Tri.Vert.vertSize * Float.BYTES, 7 * Float.BYTES);
            gl.glEnableVertexAttribArray(2);

            gl.glVertexAttribPointer(3, 4, GL.GL_FLOAT, false, Tri.Vert.vertSize * Float.BYTES, 11 * Float.BYTES);
            gl.glEnableVertexAttribArray(3);

            gl.glVertexAttribPointer(4, 4, GL.GL_FLOAT, false, Tri.Vert.vertSize * Float.BYTES, 14 * Float.BYTES);
            gl.glEnableVertexAttribArray(4);

            loaded = true;
        }
    }

    public int getmTVBO() {
        return mTVBO;
    }

    public int getmVAO() {
        return mVAO;
    }

    public int getmVBO() {
        return mVBO;
    }

    public Buffer getData() {
        return data;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public void setRotationCenter(Vec3 rotationCenter) {
        this.rotationCenter = rotationCenter;
    }

    public Vec3 getRotationCenter() {
        return rotationCenter;
    }

    public void setScale(Vec3 scale) {
        this.scale = scale;
    }

    public Vec3 getScale() {
        return scale;
    }

    public void setTint(Vec3 tint) {
        this.tint = tint;
    }

    public Vec3 getTint() {
        return tint;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public static void renderAll (GL2 gl, Shader shader, Camera camera, DirectionalLight dirLight, Mesh meshes[]) {
        
    }
}
