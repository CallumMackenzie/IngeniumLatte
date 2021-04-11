package ingenium.mesh;

import ingenium.math.Vec3;
import ingenium.mesh.Triangle.Tri2D;
import ingenium.mesh.Triangle.Tri3D;
import ingenium.math.Vec2;
import ingenium.math.Mat2;
import ingenium.world.Camera2D;
import ingenium.world.Position;
import ingenium.world.Shader;

import java.nio.FloatBuffer;
import com.jogamp.opengl.GL4;

public class Mesh2D extends Mesh<Vec2, Float> {

    public void make(GL4 gl, String objPath) {
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

            int stride = Tri2D.Vert.vertByteSize;

            gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, stride, 0); // Points (4)
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, stride, 4 * floatBytes); // Texture UVs (3)
            gl.glEnableVertexAttribArray(1);

            gl.glVertexAttribPointer(2, 4, GL4.GL_FLOAT, false, stride, 7 * floatBytes); // RGBA tint (4)
            gl.glEnableVertexAttribArray(2);

            loaded = true;
        }
    }

    public void sendToShader(GL4 gl, Shader shader) {
        shader.setUVec2(gl, "translation", position);
        shader.setUVec4(gl, "modelTint", tint);
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
     * 
     * @return whether the data has been transferred to the GPU
     */
    public boolean isLoaded() {
        return loaded;
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

    public static void renderAll(GL4 gl, Shader shader, Camera2D camera, Mesh2D meshes[]) {
        shader.use(gl);
        Material.sendToShader(gl, shader);
        camera.sendToShader(gl, shader);
        shader.setUniform(gl, "u_time", (float) (System.currentTimeMillis() / 1000L));

        for (int i = 0; i < meshes.length; i++) {
            meshes[i].sendToShader(gl, shader);
            gl.glDrawArrays(GL4.GL_TRIANGLES, 0, meshes[i].numVerts);
        }
    }
}
