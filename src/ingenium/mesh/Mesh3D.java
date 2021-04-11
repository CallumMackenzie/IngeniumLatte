package ingenium.mesh;

import com.jogamp.opengl.GL4;
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
     * @param gl           the GL4 object of the program
     * @param objPath      the path to the object file
     * @param diffusePath  the path to the diffuse texture
     * @param specularPath the path to the specular texture
     * @param normalPath   the path to the normal texture
     */
    public void make(GL4 gl, String objPath, String diffusePath, String specularPath, String normalPath) {
        setTexture(gl, diffusePath, specularPath, normalPath);
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

    /**
     * Loads all the data onto the GPU
     * 
     * @param gl the GL4 object of the program
     */
    @Override
    public void load(GL4 gl) {
        if (!loaded) {
            int[] buffers = new int[1];
            int[] vertexArrays = new int[1];
            int floatBytes = Float.BYTES;

            gl.glGenBuffers(1, buffers, 0);
            gl.glGenVertexArrays(1, vertexArrays, 0);

            mVBO = buffers[0];
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, data.capacity() * floatBytes, data, GL4.GL_STATIC_DRAW);

            mVAO = vertexArrays[0];
            gl.glBindVertexArray(mVAO);
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, mVBO);

            int stride = Tri3D.Vert.vertByteSize;

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
        shader.setUVec4(gl, "meshTint", tint);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, material.getDiffuseTexture());
        gl.glActiveTexture(GL4.GL_TEXTURE1);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, material.getSpecularTexture());
        gl.glActiveTexture(GL4.GL_TEXTURE2);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, material.getNormalTexture());
    }

    /**
     * 
     * @param gl          the GL4 object of the program
     * @param shader      the shader to render the mesh with
     * @param camera      the camera
     * @param dirLight    the global light
     * @param pointLights point lights
     */
    public void render(GL4 gl, Shader shader, Camera3D camera, DirectionalLight dirLight, PointLight pointLights[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        shader.setUniform(gl, "u_time", (float) System.currentTimeMillis() / 1000.f);
        dirLight.sendToShader(gl, shader);

        for (int i = 0; i < pointLights.length; i++)
            pointLights[i].sendToShader(gl, shader, i);

        sendToShader(gl, shader);

        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, numVerts);
    }

    /**
     * 
     * @param gl       the GL4 object of the program
     * @param shader   the shader to render the mesh with
     * @param camera   the camera
     * @param dirLight the global light
     */
    public void render(GL4 gl, Shader shader, Camera3D camera, DirectionalLight dirLight) {
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
    public static void renderAll(GL4 gl, Shader shader, Camera3D camera, DirectionalLight dirLight, Mesh3D meshes[],
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
            gl.glDrawArrays(GL4.GL_TRIANGLES, 0, meshes[i].numVerts);
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
    public static void renderAll(GL4 gl, Shader shader, Camera3D camera, DirectionalLight dirLight, Mesh3D meshes[]) {
        Mesh3D.renderAll(gl, shader, camera, dirLight, meshes, new PointLight[] {});
    }

}
