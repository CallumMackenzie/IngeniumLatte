package ingenium.mesh;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import ingenium.Utils;
import ingenium.math.*;
import ingenium.world.Camera;
import ingenium.world.Position3D;
import ingenium.world.Shader;
import ingenium.world.light.DirectionalLight;
import ingenium.world.light.PointLight;

public class Mesh extends Position3D {
    private static boolean cacheGeometry = false;
    private static boolean cacheTextures = false;

    private static HashMap<String, Integer> cachedTextures = new HashMap<String, Integer>();
    private static HashMap<String, Integer> cachedGeometry = new HashMap<String, Integer>();

    private Vec3 scale;
    private Vec3 tint = new Vec3();
    private Material material;

    private boolean loaded = false;
    private FloatBuffer data;
    private int mVBO = GL4.GL_NONE;
    private int mVAO = GL4.GL_NONE;

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     * @param material      the material
     */
    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationPoint, Material material) {
        this.rotation = rotation;
        this.rotationPoint = rotationPoint;
        this.position = position;
        this.scale = scale;
        this.material = material;
    }

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     */
    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationPoint) {
        this(position, rotation, scale, rotationPoint, new Material());
    }

    /**
     * 
     * @param position the position
     * @param rotation the rotation
     * @param scale    the scale
     */
    public Mesh(Vec3 position, Vec3 rotation, Vec3 scale) {
        this(position, rotation, scale, new Vec3());
    }

    /**
     * 
     * @param position the position
     * @param rotation the rotation
     */
    public Mesh(Vec3 position, Vec3 rotation) {
        this(position, rotation, new Vec3(1.f, 1.f, 1.f));
    }

    /**
     * 
     * @param position the position
     */
    public Mesh(Vec3 position) {
        this(position, new Vec3());
    }

    /**
     * 
     */
    public Mesh() {
        this(new Vec3());
    }

    /**
     * 
     * @param raw the raw obj data in string form
     */
    private void loadFromObjData(String raw, boolean path) {
        if (path)
            raw = Utils.getFileAsString(raw);
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
        if (path)
            if (cacheGeometry)
                if (cachedGeometry.containsKey(path))
    }

    public void loadFromObjData(String path) {
        loadFromObjData(path, true);
    }

    /**
     * Loads the specified images onto the GPU, and their locations into the
     * material associated with the mesh
     * 
     * @param gl           the GL4 object of this program
     * @param diffusePath  the path to the diffuse texture
     * @param specularPath the path to the specular texture
     * @param normalPath   the path to the normal texture
     */
    public void setTexture(GL4 gl, String diffusePath, String specularPath, String normalPath) {
        if (!diffusePath.equals(Utils.NO_VALUE))
            material.setDiffuseTexture(findAndLoadTexture(gl, diffusePath, GL4.GL_TEXTURE0));
        if (!specularPath.equals(Utils.NO_VALUE))
            material.setSpecularTexture(findAndLoadTexture(gl, specularPath, GL4.GL_TEXTURE1));
        if (!normalPath.equals(Utils.NO_VALUE))
            material.setNormalTexture(findAndLoadTexture(gl, normalPath, GL4.GL_TEXTURE2));
    }

    /**
     * 
     * @param gl           the GL4 object of the program
     * @param diffusePath  the path to the diffuse texture
     * @param specularPath the path to the specular texture
     * @param normalPath   the path to the normal texture
     */
    public void make(GL4 gl, String diffusePath, String specularPath, String normalPath) {

    }

    /**
     * Loads all the data onto the GPU
     * 
     * @param gl the GL4 object of the program
     */
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

            loaded = true;
        }
    }

    /**
     * 
     * @return the 3D transformation of the model
     */
    public Mat4 modelMatrix() {
        Mat4 matRot = Mat4.rotationOnPoint(rotation.getX(), rotation.getY(), rotation.getZ(), rotationPoint);
        Mat4 matTrans = Mat4.translation(position.getX(), position.getY(), position.getZ());
        Mat4 matScale = Mat4.scale(scale.getX(), scale.getY(), scale.getZ());
        Mat4 matWorld = matScale.mul(matRot, matTrans);
        return matWorld;
    }

    /**
     * Binds the VBO
     * 
     * @param gl the GL4 object of the program
     */
    public void bindVBO(GL4 gl) {
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);
    }

    /**
     * Binds the VAO
     * 
     * @param gl the GL4 object of the program
     */
    public void bindVAO(GL4 gl) {
        gl.glBindVertexArray(mVAO);
    }

    /**
     * 
     * @return the data buffer
     */
    public FloatBuffer getData() {
        return data;
    }

    /**
     * 
     * @return whether the data has been transferred to the GPU
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * 
     * @param scale the scale to set
     */
    public void setScale(Vec3 scale) {
        this.scale = scale;
    }

    /**
     * 
     * @return the scale of the mesh
     */
    public Vec3 getScale() {
        return scale;
    }

    /**
     * 
     * @param tint the tint to set
     */
    public void setTint(Vec3 tint) {
        this.tint = tint;
    }

    /**
     * 
     * @return the tint of the mesh
     */
    public Vec3 getTint() {
        return tint;
    }

    /**
     * 
     * @param material the material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * 
     * @return the material of the mesh
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * 
     * @param gl     the GL4 object of the program
     * @param shader the shader to send data to
     */
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

    /**
     * 
     * @param gl          the GL4 object of the program
     * @param shader      the shader to render the mesh with
     * @param camera      the camera
     * @param dirLight    the global light
     * @param pointLights point lights
     */
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

    /**
     * 
     * @param gl       the GL4 object of the program
     * @param shader   the shader to render the mesh with
     * @param camera   the camera
     * @param dirLight the global light
     */
    public void render(GL4 gl, Shader shader, Camera camera, DirectionalLight dirLight) {
        render(gl, shader, camera, dirLight, new PointLight[0]);
    }

    /**
     * 
     * @param gl          the GL4 object of the program
     * @param shader      the shader to render the mesh with
     * @param camera      the camera
     * @param dirLight    the global light
     * @param meshes      the meshes to render
     * @param pointLights point lights
     */
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

    /**
     * 
     * @param gl       the GL4 object of the program
     * @param shader   the shader to render the mesh with
     * @param camera   the camera
     * @param dirLight the global light
     * @param meshes   the meshes to render
     */
    public static void renderAll(GL4 gl, Shader shader, Camera camera, DirectionalLight dirLight, Mesh meshes[]) {
        Mesh.renderAll(gl, shader, camera, dirLight, meshes, new PointLight[] {});
    }

    /**
     * 
     * @param gl        the GL4 object of the program
     * @param path      the path to the image
     * @param texSlot   the texture slot to use
     * @param sWrap     texture wrap s mode
     * @param tWrap     texture wrap t mode
     * @param minFilter min filter mode
     * @param magFilter mag filter mode
     * @return a new texture
     */
    private static int findAndLoadTexture(GL4 gl, String path, int texSlot, int sWrap, int tWrap, int minFilter,
            int magFilter) {
        if (cacheTextures)
            if (cachedTextures.containsKey(path))
                if (cachedTextures.get(path) != GL4.GL_NONE)
                    return cachedTextures.get(path);
        try {
            gl.glActiveTexture(texSlot);
            Texture t = TextureIO.newTexture(new File(path), true);
            t.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_S, sWrap);
            t.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_T, tWrap);
            t.setTexParameteri(gl, GL4.GL_TEXTURE_MIN_FILTER, minFilter);
            t.setTexParameteri(gl, GL4.GL_TEXTURE_MAG_FILTER, magFilter);
            if (cacheTextures)
                cachedTextures.put(path, t.getTextureObject());
            return t.getTextureObject();
        } catch (IOException e) {
            System.err.println("Could not find image: " + path + ". Terminating...");
            System.exit(0);
            return GL4.GL_NONE;
        }
    }

    /**
     * 
     * @param gl      the GL4 object of the program
     * @param path    the path to the image
     * @param texSlot the texture slot to use
     * @return a new texture
     */
    private static int findAndLoadTexture(GL4 gl, String path, int texSlot) {
        return findAndLoadTexture(gl, path, texSlot, GL4.GL_REPEAT, GL4.GL_REPEAT, GL4.GL_LINEAR_MIPMAP_LINEAR,
                GL4.GL_LINEAR);
    }

    /**
     * 
     * @return whether the mesh class is caching geometry data
     */
    public static boolean cachingGeometry() {
        return cacheGeometry;
    }

    /**
     * 
     * @return whether the mesh class is caching image data
     */
    public static boolean cachingTextures() {
        return cacheTextures;
    }

    /**
     * 
     * @param cache whether geometry data should be cached
     */
    public static void cacheGeometry(boolean cache) {
        cacheGeometry = cache;
    }

    /**
     * 
     * @param cache whether texture data should be cached
     */
    public static void cacheTextures(boolean cache) {
        cacheTextures = cache;
    }
}
