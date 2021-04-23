package ingenium.mesh;

import com.jogamp.opengl.GL2;

import ingenium.world.Shader;

public class Material {
    private int diffuseTexture = GL2.GL_NONE;
    private int specularTexture = GL2.GL_NONE;
    private int normalTexture = GL2.GL_NONE;
    private int parallaxTexture = GL2.GL_NONE;
    private float shininess = 0.5f;
    private float parallaxScale = 1.f;

    /**
     * 
     * @param shininess       the shininess of the material
     * @param parallaxScale   the scale for the parallax texture
     * @param diffuseTexture  the diffuse texture location
     * @param specularTexture the specular texture location
     * @param normalTexture   the normal texture location
     * @param parallaxTexture the parallax texture location
     */
    public Material(float shininess, float parallaxScale, int diffuseTexture, int specularTexture, int normalTexture,
            int parallaxTexture) {
        this.shininess = shininess;
        this.parallaxScale = parallaxScale;
        this.diffuseTexture = diffuseTexture;
        this.specularTexture = specularTexture;
        this.normalTexture = normalTexture;
        this.parallaxTexture = parallaxTexture;
    }

    /**
     * 
     * @param shininess the shininess of the material
     */
    public Material(float shininess) {
        this.shininess = shininess;
    }

    /**
     * 
     * @param shininess the shininess of the material
     */
    public Material(double shininess) {
        this.shininess = (float) shininess;
    }

    /**
     * Constructs a default empty material
     */
    public Material() {

    }

    /**
     * 
     * @return the diffuse texture location
     */
    public int getDiffuseTexture() {
        return diffuseTexture;
    }

    /**
     * 
     * @param diffuseTexture the diffuse texture location to set
     */
    public void setDiffuseTexture(int diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    /**
     * 
     * @return the normal texture location
     */
    public int getNormalTexture() {
        return normalTexture;
    }

    /**
     * 
     * @param normalTexture the normal texture location to set
     */
    public void setNormalTexture(int normalTexture) {
        this.normalTexture = normalTexture;
    }

    /**
     * 
     * @return the specular texture location
     */
    public int getSpecularTexture() {
        return specularTexture;
    }

    /**
     * 
     * @param specularTexture the specular texture location to set
     */
    public void setSpecularTexture(int specularTexture) {
        this.specularTexture = specularTexture;
    }

    /**
     * 
     * @param parallaxScale the parallax scale to set
     */
    public void setParallaxScale(float parallaxScale) {
        this.parallaxScale = parallaxScale;
    }

    /**
     * 
     * @param parallaxScale the parallax scale to set
     */
    public void setParallaxScale(double parallaxScale) {
        this.parallaxScale = (float) parallaxScale;
    }

    /**
     * 
     * @return the parallax scale
     */
    public float getParallaxScale() {
        return parallaxScale;
    }

    /**
     * 
     * @param parallaxTexture the parallax texture location to set
     */
    public void setParallaxTexture(int parallaxTexture) {
        this.parallaxTexture = parallaxTexture;
    }

    /**
     * 
     * @return the parallax texture location
     */
    public int getParallaxTexture() {
        return parallaxTexture;
    }

    /**
     * 
     * @param shininess the shininess to set
     */
    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    /**
     * 
     * @param shininess the shininess to set
     */
    public void setShininess(double shininess) {
        this.shininess = (float) shininess;
    }

    /**
     * 
     * @return the shininess
     */
    public float getShininess() {
        return shininess;
    }

    public void sendDataToShader(GL2 gl, Shader shader) {
        shader.setUniform(gl, Shader.Uniforms.material_shininess, getShininess());
        shader.setUniform(gl, Shader.Uniforms.material_heightScale, getParallaxScale());
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, getDiffuseTexture());
        gl.glActiveTexture(GL2.GL_TEXTURE1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, getSpecularTexture());
        gl.glActiveTexture(GL2.GL_TEXTURE2);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, getNormalTexture());
    }

    /**
     * 
     * @param gl     the GL2 object of the program
     * @param shader the shader to send the information to
     */
    public static void sendToShader(GL2 gl, Shader shader) {
        shader.setUniform(gl, Shader.Uniforms.material_diffuse, 0);
        shader.setUniform(gl, Shader.Uniforms.material_specular, 1);
        shader.setUniform(gl, Shader.Uniforms.material_normal, 2);
        shader.setUniform(gl, Shader.Uniforms.material_parallax, 3);
    }
}
