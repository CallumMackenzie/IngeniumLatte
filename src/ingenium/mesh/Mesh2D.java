package ingenium.mesh;

import ingenium.math.Vec3;
import ingenium.mesh.Triangle.Tri2D;
import ingenium.Ingenium;
import ingenium.math.Mat2;
import ingenium.math.Vec2;
import ingenium.world.Camera2D;
import ingenium.world.Shader;
import com.jogamp.opengl.GL2;

public class Mesh2D extends Mesh<Vec2, Float> {
    private float zIndex = 0;

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     * @param material      the material
     */
    public Mesh2D(Vec2 position, float rotation, Vec2 scale, Vec2 rotationPoint, Material material) {
        super(position, rotation, scale, rotationPoint, material);
    }

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     */
    public Mesh2D(Vec2 position, float rotation, Vec2 scale, Vec2 rotationPoint) {
        this(position, rotation, scale, rotationPoint, new Material());
    }

    /**
     * 
     * @param position the position
     * @param rotation the rotation
     * @param scale    the scale
     */
    public Mesh2D(Vec2 position, float rotation, Vec2 scale) {
        this(position, rotation, scale, new Vec2());
    }

    /**
     * 
     * @param position the position
     * @param rotation the rotation
     */
    public Mesh2D(Vec2 position, float rotation) {
        this(position, rotation, new Vec2(1.f, 1.f));
    }

    /**
     * 
     * @param position the position
     */
    public Mesh2D(Vec2 position) {
        this(position, 0f);
    }

    /**
     * 
     */
    public Mesh2D() {
        this(new Vec2());
    }

    public void make(GL2 gl, String path, String diffusePath) {
        setTexture(gl, diffusePath, Ingenium.NO_VALUE, Ingenium.NO_VALUE);
        Geometry.Object object3d = Geometry.loadFromObjData2D(path, true, useGeometryReferenceCache,
                useGeometryValueCache);
        if (object3d.isReference()) {
            this.mVBO = object3d.getVBO();
            this.mVAO = object3d.getVAO();
            loaded = true;
        } else
            this.data = object3d.getData();
        this.numVerts = object3d.getNumVerts();
        load(gl);
    }

    public void make(GL2 gl, String diffusePath) {
        setTexture(gl, diffusePath, Ingenium.NO_VALUE, Ingenium.NO_VALUE);
        Geometry.Object object3d = Geometry.make2DQuad();
        if (object3d.isReference()) {
            this.mVBO = object3d.getVBO();
            this.mVAO = object3d.getVAO();
            loaded = true;
        } else
            this.data = object3d.getData();
        this.numVerts = object3d.getNumVerts();
        load(gl);
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

            int stride = Tri2D.Vert.vertByteSize;

            gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, stride, 0); // Points (2)
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 2, GL2.GL_FLOAT, false, stride, 2 * floatBytes); // Texture UVs (2)
            gl.glEnableVertexAttribArray(1);

            gl.glVertexAttribPointer(2, 4, GL2.GL_FLOAT, false, stride, 4 * floatBytes); // RGBA tint (4)
            gl.glEnableVertexAttribArray(2);

            loaded = true;
        }
    }

    public Mat2 modelMatrix() {
        return Mat2.rotation(this.rotation);
    }

    public void sendToShader(GL2 gl, Shader shader) {
        shader.setUVec4(gl, Shader.Uniforms.mesh2D_tint, this.tint);
        shader.setUVec2(gl, Shader.Uniforms.mesh2D_translation, this.position);
        shader.setUMat2(gl, Shader.Uniforms.mesh2D_rotation, modelMatrix());
        shader.setUVec2(gl, Shader.Uniforms.mesh2D_rotationPoint, this.rotationPoint);
        shader.setUVec2(gl, Shader.Uniforms.mesh2D_scale, this.scale);
        shader.setUniform(gl, Shader.Uniforms.mesh2D_zIndex, zIndex);

        this.material.sendDataToShader(gl, shader);
    }

    /**
     * Binds the VBO
     * 
     * @param gl the GL2 object of the program
     */
    public void bindVBO(GL2 gl) {
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, mVBO);
    }

    /**
     * 
     * @return whether the data has been transferred to the GPU
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Binds the VAO
     * 
     * @param gl the GL2 object of the program
     */
    public void bindVAO(GL2 gl) {
        gl.glBindVertexArray(mVAO);
    }

    /**
     * 
     * @param scale the scale to set
     */
    public void setScale(Vec2 scale) {
        this.scale = scale;
    }

    /**
     * 
     * @return the scale of the mesh
     */
    public Vec2 getScale() {
        return scale;
    }

    /**
     * 
     * @param zIndex the z index
     */
    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * 
     * @return the z index
     */
    public float getZIndex() {
        return zIndex;
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
     * @param gl     the GL2 object of the program
     * @param shader the shader to use
     * @param camera the 2D camera to use
     * @param meshes the meshes to render
     */
    public static void renderAll(GL2 gl, Shader shader, Camera2D camera, Mesh2D meshes[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        shader.setUniform(gl, Shader.Uniforms.ingenium_time, (float) (System.currentTimeMillis() / 1000L));

        for (int i = 0; i < meshes.length; i++) {
            meshes[i].bindVAO(gl);
            meshes[i].sendToShader(gl, shader);
            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, meshes[i].numVerts);
        }
    }
}
