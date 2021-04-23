package ingenium.mesh;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import ingenium.Ingenium;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.utilities.Cache;
import ingenium.world.Position;

public class Mesh<positionType, rotationType> extends Position<positionType, rotationType> implements Loadable {
    protected static final Cache<String, Integer> textureReferenceCache = new Cache<>("geometry cache", false);

    protected Vec3 tint = new Vec3(1, 1, 1);
    protected positionType scale;
    protected Material material;
    protected FloatBuffer data = null;
    protected boolean loaded = false;
    protected int mVBO = GL2.GL_NONE;
    protected int mVAO = GL2.GL_NONE;
    protected int numVerts = 0;
    protected boolean useTextureReferenceCache = true;
    protected boolean useGeometryReferenceCache = true;
    protected boolean useGeometryValueCache = true;

    /**
     * 
     * @param position      the position
     * @param rotation      the rotation
     * @param scale         the scale
     * @param rotationPoint the relative point rotation is done around
     * @param material      the material
     */
    public Mesh(positionType position, rotationType rotation, positionType scale, positionType rotationPoint,
            Material material) {
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
    public Mesh(positionType position, rotationType rotation, positionType scale, positionType rotationPoint) {
        this(position, rotation, scale, rotationPoint, new Material());
    }

    public void load(GL2 gl) {
        System.err.println("A class extending ingenium.mesh.Mesh must implement a load() method.");
        System.exit(0);
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
     * Binds the VAO
     * 
     * @param gl the GL2 object of the program
     */
    public void bindVAO(GL2 gl) {
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
    public void setScale(positionType scale) {
        this.scale = scale;
    }

    /**
     * 
     * @return the scale of the mesh
     */
    public positionType getScale() {
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
     * @return the texture reference cache
     */
    public static Cache<String, Integer> getTextureReferenceCache() {
        return textureReferenceCache;
    }

    /**
     * 
     * @return the number of vertecies in the mesh
     */
    public int getNumVerts() {
        return numVerts;
    }

    /**
     * 
     * @param useGeometryReferenceCache whether to check the geometry reference
     *                                  cache
     */
    public void useGeometryReferenceCache(boolean useGeometryReferenceCache) {
        this.useGeometryReferenceCache = useGeometryReferenceCache;
    }

    /**
     * 
     * @param useGeometryValueCache whether to check the geometry value cache
     */
    public void useGeometryValueCache(boolean useGeometryValueCache) {
        this.useGeometryValueCache = useGeometryValueCache;
    }

    /**
     * 
     * @param useTextureReferenceCache whether to check the texture reference cache
     */
    public void useTextureReferenceCache(boolean useTextureReferenceCache) {
        this.useTextureReferenceCache = useTextureReferenceCache;
    }

    /**
     * 
     * @return whether the texture reference cache is being used
     */
    public boolean usingTextureReferenceCache() {
        return useTextureReferenceCache;
    }

    /**
     * 
     * @return whether the geometry value cache is being used
     */
    public boolean usingGeometryValueCache() {
        return useGeometryValueCache;
    }

    /**
     * 
     * @return whether the geometry reference cache is being used
     */
    public boolean usingGeometryReferenceCache() {
        return useGeometryReferenceCache;
    }

    /**
     * 
     * @param gl        the GL2 object of the program
     * @param path      the path to the image
     * @param texSlot   the texture slot to use
     * @param sWrap     texture wrap s mode
     * @param tWrap     texture wrap t mode
     * @param minFilter min filter mode
     * @param magFilter mag filter mode
     * @return a new texture
     */
    private static int findAndLoadTexture(GL2 gl, String path, int texSlot, int sWrap, int tWrap, int minFilter,
            int magFilter, boolean useTexCache) {
        if (path.equals(Ingenium.NO_VALUE))
            return GL2.GL_NONE;
        boolean debug = false;
        long time = System.currentTimeMillis();
        if (textureReferenceCache.isUsed() && textureReferenceCache.containsKey(path)
                && textureReferenceCache.getCacheValue(path) != GL2.GL_NONE && useTexCache) {
            if (debug)
                System.out.println(
                        "Texture reference cache hit (" + path + "): " + (System.currentTimeMillis() - time) + "ms");
            return textureReferenceCache.getCacheValue(path);
        }
        try {
            gl.glActiveTexture(texSlot);
            Texture t = TextureIO.newTexture(new File(path), true);
            t.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, sWrap);
            t.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, tWrap);
            t.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, minFilter);
            t.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, magFilter);
            textureReferenceCache.add(path, t.getTextureObject());
            if (debug)
                System.out.println("No cache hits (" + path + "): " + (System.currentTimeMillis() - time) + "ms");
            return t.getTextureObject();
        } catch (IOException e) {
            System.err.println("Could not find image: " + path);
            return GL2.GL_NONE;
        }
    }

    public static int createColorTexture (GL2 gl, int hex, float alpha) {
        float r = (float) ((hex & 0xFF0000) >> 16) / 255.f;
        float g = (float) ((hex & 0x00FF00) >> 8) / 255.f;
        float b = (float) ((hex & 0x0000FF)) / 255.f;
        return createColorTexture(gl, r, g, b, alpha);
    }

    public static int createColorTexture (GL2 gl, float r, float g, float b, float alpha) {
        int texs[] = new int[1];
        gl.glGenTextures(1, texs, 0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texs[0]);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, 1, 1, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, Buffers.newDirectFloatBuffer(new float[]{r, g, b, alpha}));
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        return texs[0];
    }

    /**
     * Loads the specified images onto the GPU, and their locations into the
     * material associated with the mesh
     * 
     * @param gl           the GL2 object of this program
     * @param diffusePath  the path to the diffuse texture
     * @param specularPath the path to the specular texture
     * @param normalPath   the path to the normal texture
     */
    public void setTexture(GL2 gl, String diffusePath, String specularPath, String normalPath) {
        if (!diffusePath.equals(Ingenium.NO_VALUE))
            material.setDiffuseTexture(findAndLoadTexture(gl, diffusePath, GL2.GL_TEXTURE0, useTextureReferenceCache));
        if (!specularPath.equals(Ingenium.NO_VALUE))
            material.setSpecularTexture(
                    findAndLoadTexture(gl, specularPath, GL2.GL_TEXTURE1, useTextureReferenceCache));
        if (!normalPath.equals(Ingenium.NO_VALUE))
            material.setNormalTexture(findAndLoadTexture(gl, normalPath, GL2.GL_TEXTURE2, useTextureReferenceCache));
        else
            material.setNormalTexture(createColorTexture(gl, 0f, 0f, 1f, 1f));
    }

    /**
     * 
     * @param gl      the GL2 object of the program
     * @param path    the path to the image
     * @param texSlot the texture slot to use
     * @return a new texture
     */
    private static int findAndLoadTexture(GL2 gl, String path, int texSlot, boolean useTexCache) {
        return findAndLoadTexture(gl, path, texSlot, GL2.GL_REPEAT, GL2.GL_REPEAT, GL2.GL_LINEAR_MIPMAP_LINEAR,
                GL2.GL_LINEAR, useTexCache);
    }
}
