package ingenium.mesh;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import ingenium.math.*;
import ingenium.world.Camera;
import ingenium.world.Position3D;
import ingenium.world.Shader;
import ingenium.world.light.DirectionalLight;
import ingenium.world.light.PointLight;

public class Mesh extends Position3D {
    private Vec3 scale;
    private Vec3 tint = new Vec3();
    private Material material;

    private boolean loaded = false;
    private FloatBuffer data;
    private int mVBO = GL4.GL_NONE;
    private int mVAO = GL4.GL_NONE;

    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationPoint, Material material) {
        this.rotation = rotation;
        this.rotationPoint = rotationPoint;
        this.position = position;
        this.scale = scale;
        this.material = material;
    }

    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationPoint) {
        this(position, rotation, scale, rotationPoint, new Material());
    }

    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale) {
        this(position, rotation, scale, new Vec3());
    }

    public Mesh(Vec3 position, Vec3 rotation) {
        this(position, rotation, new Vec3(1.f, 1.f, 1.f));
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
            for (int k = 0; k < 3; k++) {
                Tri.Vert v = t.getVert(k);
                v.setRgb(v.getRgb().add(tint));
                t.setVert(k, v);
            }
            float triDat[] = t.toDataArray(this);
            int datLen = dataToWrite.length;
            int triLen = triDat.length;
            float newData[] = new float[datLen + triLen];
            System.arraycopy(dataToWrite, 0, newData, 0, datLen);
            System.arraycopy(triDat, 0, newData, datLen, triLen);
            dataToWrite = newData;
        }
        data = Buffers.newDirectFloatBuffer(dataToWrite);
    }

    public void load(GL4 gl) {
        if (!loaded) {
            int[] buffers = new int[1];
            int[] vertexArrays = new int[1];
            int floatBytes = Float.BYTES;

            gl.glGenBuffers(1, buffers, 0);
            gl.glGenVertexArrays(1, vertexArrays, 0);

            mVBO = buffers[0];
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);
            long len = data.capacity() * floatBytes;
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, len, data, GL4.GL_STATIC_DRAW);

            mVAO = vertexArrays[0];
            gl.glBindVertexArray(mVAO);
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);

            int stride = Tri.Vert.vertSize * floatBytes;

            gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, stride, 0); // Points (4)
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, stride, 4 * floatBytes); // Texture UVs (3)
            gl.glEnableVertexAttribArray(1);

            gl.glVertexAttribPointer(2, 4, GL4.GL_FLOAT, false, stride, 7 * floatBytes); // RGBA tint (4)
            gl.glEnableVertexAttribArray(2);

            gl.glVertexAttribPointer(3, 3, GL4.GL_FLOAT, false, stride, 11 * floatBytes); // Normals (3)
            gl.glEnableVertexAttribArray(3);

            gl.glVertexAttribPointer(4, 3, GL4.GL_FLOAT, false, stride, 14 * floatBytes); // Tangents (3)
            gl.glEnableVertexAttribArray(4);

            FloatBuffer b2 = Buffers.newDirectFloatBuffer(new float[data.capacity()]);
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);
            gl.glGetBufferSubData(GL4.GL_ARRAY_BUFFER, 0L, data.capacity() * Buffers.SIZEOF_FLOAT, b2);
            boolean eq = b2.capacity() == data.capacity();
            if (eq)
                for (int x = 0; x < data.capacity(); x++)
                    eq = eq && data.get(x) == b2.get(x);

            System.out.println("Equal? " + (eq ? "true" : "false"));

            loaded = true;
        }
    }

    public Mat4 modelMatrix() {
        Mat4 matRot = Mat4.rotationOnPoint(rotation.getX(), rotation.getY(), rotation.getZ(), rotationPoint);
        Mat4 matTrans = Mat4.translation(position.getX(), position.getY(), position.getZ());
        Mat4 matScale = Mat4.scale(scale.getX(), scale.getY(), scale.getZ());
        Mat4 matWorld = matScale.mul(matRot, matTrans);
        return matWorld;
    }

    public void bindVBO(GL4 gl) {
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);
    }

    public void bindVAO(GL4 gl) {
        gl.glBindVertexArray(mVAO);
    }

    public Buffer getData() {
        return data;
    }

    public boolean isLoaded() {
        return loaded;
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

    public void sendToShader(GL4 gl, Shader shader) {
        bindVAO(gl);
        bindVBO(gl);
        Mat4 model = modelMatrix();
        shader.setUniform(gl, "model", model);
        shader.setUniform(gl, "invModel", model.inverse());
        shader.setUniform(gl, "material.shininess", material.getShininess());
        shader.setUniform(gl, "heightScale", material.getParallaxScale());

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        if (material.getDiffuseTexture() != GL4.GL_NONE) {
            gl.glBindTexture(GL4.GL_TEXTURE_2D, material.getDiffuseTexture());
        }
        gl.glActiveTexture(GL4.GL_TEXTURE1);
        if (material.getSpecularTexture() != GL4.GL_NONE) {
            gl.glBindTexture(GL4.GL_TEXTURE_2D, material.getSpecularTexture());
        }
        gl.glActiveTexture(GL4.GL_TEXTURE2);
        if (material.getNormalTexture() != GL4.GL_NONE) {
            gl.glBindTexture(GL4.GL_TEXTURE_2D, material.getNormalTexture());
        }
    }

    public void render(GL4 gl, Shader shader, Camera camera, DirectionalLight dirLight, PointLight pointLights[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        shader.setUniform(gl, "u_time", (float) System.currentTimeMillis() / 1000.f);
        dirLight.sendToShader(gl, shader);

        for (int i = 0; i < pointLights.length; i++)
            pointLights[i].sendToShader(gl, shader, i);

        sendToShader(gl, shader);

        int toddraw = data.capacity() / Tri.Vert.vertSize;
        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, toddraw);
    }

    public void render(GL4 gl, Shader shader, Camera camera, DirectionalLight dirLight) {
        render(gl, shader, camera, dirLight, new PointLight[0]);
    }

    public static void renderAll(GL4 gl, Shader shader, Camera camera, DirectionalLight dirLight, Mesh meshes[],
            PointLight pointLights[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        dirLight.sendToShader(gl, shader);
        shader.setUniform(gl, "u_time", (float) (System.currentTimeMillis() / 1000L));

        for (int i = 0; i < pointLights.length; i++)
            pointLights[i].sendToShader(gl, shader, i);

        for (int i = 0; i < meshes.length; i++) {
            meshes[i].sendToShader(gl, shader);

            gl.glDrawArrays(GL4.GL_TRIANGLES, 0, meshes[i].data.capacity() / Tri.Vert.vertSize);
        }
    }

    public static void renderAll(GL4 gl, Shader shader, Camera camera, DirectionalLight dirLight, Mesh meshes[]) {
        Mesh.renderAll(gl, shader, camera, dirLight, meshes, new PointLight[] {});
    }
}
