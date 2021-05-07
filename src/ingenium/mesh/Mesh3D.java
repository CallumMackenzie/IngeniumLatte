package ingenium.mesh;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

import ingenium.Ingenium;
import ingenium.math.*;
import ingenium.mesh.Triangle.Tri3D;
import ingenium.world.*;
import ingenium.world.light.*;

public class Mesh3D extends Mesh<Vec3, Vec3> {

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     * @param material      the material
     */
    public Mesh3D(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationPoint, Material material) {
        super(position, rotation, scale, rotationPoint, material);
    }

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     */
    public Mesh3D(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationPoint) {
        this(position, rotation, scale, rotationPoint, new Material());
    }

    /**
     * 
     * @param position the position
     * @param rotation the rotation
     * @param scale    the scale
     */
    public Mesh3D(Vec3 position, Vec3 rotation, Vec3 scale) {
        this(position, rotation, scale, new Vec3());
    }

    /**
     * 
     * @param position the position
     * @param rotation the rotation
     */
    public Mesh3D(Vec3 position, Vec3 rotation) {
        this(position, rotation, new Vec3(1.f, 1.f, 1.f));
    }

    /**
     * 
     * @param position the position
     */
    public Mesh3D(Vec3 position) {
        this(position, new Vec3());
    }

    /**
     * 
     */
    public Mesh3D() {
        this(new Vec3());
    }

    /**
     * 
     * @param gl           the GL2 object of the program
     * @param objPath      the path to the object file
     * @param diffusePath  the path to the diffuse texture
     * @param specularPath the path to the specular texture
     * @param normalPath   the path to the normal texture
     */
    public void make(GL2 gl, String objPath, String diffusePath, String specularPath, String normalPath,
            String parallaxPath) {
        setTexture(gl, diffusePath, specularPath, normalPath, parallaxPath);
        Geometry.Object object3d = Geometry.loadFromObjData(objPath, true, useGeometryReferenceCache,
                useGeometryValueCache);
        if (object3d.isReference()) {
            this.mVBO = object3d.getVBO();
            this.mVAO = object3d.getVAO();
            loaded = true;
        } else
            this.data = object3d.getData();
        this.numVerts = object3d.getNumVerts();
        load(gl);
        Geometry.getReferenceCache().checkAddCache(objPath, new Integer[] { mVBO, mVAO, numVerts });
    }

    public void makePreloaded(GL2 gl, String preloadedPath, String diffusePath, String specularPath, String normalPath,
            String parallaxPath) {
        setTexture(gl, diffusePath, specularPath, normalPath, parallaxPath);
        Geometry.Object object3d = Geometry.loadFromPreloadedModel(preloadedPath, useGeometryValueCache,
                useGeometryReferenceCache);
        if (object3d.isReference()) {
            this.mVBO = object3d.getVBO();
            this.mVAO = object3d.getVAO();
            loaded = true;
        } else
            this.data = object3d.getData();
        this.numVerts = object3d.getNumVerts();
        load(gl);
        Geometry.getReferenceCache().checkAddCache(preloadedPath, new Integer[] { mVBO, mVAO, numVerts });
    }

    public void make(GL2 gl, String objPath, String diffusePath, String specularPath, String normalPath) {
        make(gl, objPath, diffusePath, specularPath, normalPath, Ingenium.NO_VALUE);
    }

    public void make(GL2 gl, String objPath, String diffusePath, String specularPath) {
        make(gl, objPath, diffusePath, specularPath, Ingenium.NO_VALUE);
    }

    public void make(GL2 gl, String objPath, String diffusePath) {
        make(gl, objPath, diffusePath, Ingenium.NO_VALUE);
    }

    /**
     * Loads all the data onto the GPU
     * 
     * @param gl the GL2 object of the program
     */
    @Override
    public void load(GL2 gl) {
        if (!loaded) {
            int[] buffers = new int[1];
            int[] vertexArrays = new int[1];
            int floatBytes = Float.BYTES;

            gl.glGenBuffers(1, buffers, 0);
            gl.glGenVertexArrays(1, vertexArrays, 0);

            mVBO = buffers[0];
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, mVBO);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, data.capacity() * floatBytes, data, GL2.GL_STATIC_DRAW);

            mVAO = vertexArrays[0];
            gl.glBindVertexArray(mVAO);
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, mVBO);

            int stride = Tri3D.Vert.vertByteSize;

            gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, stride, 0); // Points (4)
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, stride, 4 * floatBytes); // Texture UVs (3)
            gl.glEnableVertexAttribArray(1);

            gl.glVertexAttribPointer(2, 4, GL2.GL_FLOAT, false, stride, 7 * floatBytes); // RGBA tint (4)
            gl.glEnableVertexAttribArray(2);

            gl.glVertexAttribPointer(3, 3, GL2.GL_FLOAT, false, stride, 11 * floatBytes); // Normals (3)
            gl.glEnableVertexAttribArray(3);

            gl.glVertexAttribPointer(4, 3, GL2.GL_FLOAT, false, stride, 14 * floatBytes); // Tangents (3)
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
     * 
     * @param gl     the GL2 object of the program
     * @param shader the shader to send data to
     */
    public void sendToShader(GL2 gl, Shader shader) {
        bindVAO(gl);
        bindVBO(gl);
        Mat4 model = modelMatrix();
        shader.setUniform(gl, Shader.Uniforms.mesh3D_modelMatrix, model);
        shader.setUniform(gl, Shader.Uniforms.mesh3D_invModelMatrix, model.inverse());
        shader.setUVec4(gl, Shader.Uniforms.mesh3D_tint, tint);

        this.material.sendDataToShader(gl, shader);
    }

    /**
     * 
     * @param gl          the GL2 object of the program
     * @param shader      the shader to render the mesh with
     * @param camera      the camera
     * @param dirLight    the global light
     * @param pointLights point lights
     */
    public void render(GL2 gl, Shader shader, Camera3D camera, DirectionalLight dirLight, PointLight pointLights[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        shader.setUniform(gl, Shader.Uniforms.ingenium_time, (float) System.currentTimeMillis() / 1000.f);
        dirLight.sendToShader(gl, shader);

        for (int i = 0; i < pointLights.length; i++)
            pointLights[i].sendToShader(gl, shader, i);

        sendToShader(gl, shader);

        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, numVerts);
    }

    /**
     * 
     * @param gl       the GL2 object of the program
     * @param shader   the shader to render the mesh with
     * @param camera   the camera
     * @param dirLight the global light
     */
    public void render(GL2 gl, Shader shader, Camera3D camera, DirectionalLight dirLight) {
        render(gl, shader, camera, dirLight, new PointLight[0]);
    }

    /**
     * 
     * @param gl          the GL2 object of the program
     * @param shader      the shader to render the mesh with
     * @param camera      the camera
     * @param dirLight    the global light
     * @param meshes      the meshes to render
     * @param pointLights point lights
     */
    public static void renderAll(GL2 gl, Shader shader, Camera3D camera, DirectionalLight dirLight, Mesh3D meshes[],
            PointLight pointLights[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        dirLight.sendToShader(gl, shader);
        shader.setUniform(gl, Shader.Uniforms.shader_numLights, pointLights.length);
        shader.setUniform(gl, Shader.Uniforms.ingenium_time, (float) (System.currentTimeMillis() / 1000L));

        for (int i = 0; i < pointLights.length; i++)
            pointLights[i].sendToShader(gl, shader, i);

        for (int i = 0; i < meshes.length; i++) {
            meshes[i].sendToShader(gl, shader);
            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, meshes[i].numVerts);
        }
    }

    /**
     * 
     * @param gl       the GL2 object of the program
     * @param shader   the shader to render the mesh with
     * @param camera   the camera
     * @param dirLight the global light
     * @param meshes   the meshes to render
     */
    public static void renderAll(GL2 gl, Shader shader, Camera3D camera, DirectionalLight dirLight, Mesh3D meshes[]) {
        Mesh3D.renderAll(gl, shader, camera, dirLight, meshes, new PointLight[] {});
    }

    public static Mesh3D createAndMake(GL2 gl, String objPath, String diffusePath, String specularPath,
            String normalPath) {
        Mesh3D m = new Mesh3D();
        m.make(gl, objPath, diffusePath, specularPath, normalPath);
        return m;
    }

    public static Mesh3D createAndMake(GL2 gl, String objPath, String diffusePath, String specularPath) {
        return createAndMake(gl, objPath, diffusePath, specularPath, Ingenium.NO_VALUE);
    }

    public static Mesh3D createAndMake(GL2 gl, String objPath, String diffusePath) {
        return createAndMake(gl, objPath, diffusePath, Ingenium.NO_VALUE);
    }

    public static Mesh3D createAndMakePreloaded(GL2 gl, String preloadedPath, String diffusePath, String specularPath,
            String normalPath) {
        Mesh3D m = new Mesh3D();
        m.makePreloaded(gl, preloadedPath, diffusePath, specularPath, normalPath, Ingenium.NO_VALUE);
        return m;
    }

    public static Mesh3D createAndMakePreloaded(GL2 gl, String preloadedPath, String diffusePath, String specularPath) {
        return createAndMakePreloaded(gl, preloadedPath, diffusePath, specularPath, Ingenium.NO_VALUE);
    }

    public static Mesh3D createAndMakePreloaded(GL2 gl, String preloadedPath, String diffusePath) {
        return createAndMakePreloaded(gl, preloadedPath, diffusePath, Ingenium.NO_VALUE);
    }

    public static Mesh3D createEmpty (GL2 gl, int numVerts) {
        Mesh3D m = new Mesh3D();
        m.setData(Buffers.newDirectFloatBuffer(numVerts * Tri3D.Vert.vertSize));
        m.load(gl);
        return m;
    }
}
