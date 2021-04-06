package ingenium.mesh;

import com.jogamp.opengl.GL4;

import ingenium.world.Shader;

public class Material {
    private int diffuseTexture = GL4.GL_NONE;
    private int specularTexture = GL4.GL_NONE;
    private int normalTexture = GL4.GL_NONE;
    private int parallaxTexture = GL4.GL_NONE;
    private float shininess = 0.5f;
    private float parallaxScale = 1.f;

    public int getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(int diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    public int getNormalTexture() {
        return normalTexture;
    }

    public void setNormalTexture(int normalTexture) {
        this.normalTexture = normalTexture;
    }

    public int getSpecularTexture() {
        return specularTexture;
    }

    public void setSpecularTexture(int specularTexture) {
        this.specularTexture = specularTexture;
    }

    public void setParallaxScale(float parallaxScale) {
        this.parallaxScale = parallaxScale;
    }

    public void setParallaxScale(double parallaxScale) {
        this.parallaxScale = (float) parallaxScale;
    }

    public float getParallaxScale() {
        return parallaxScale;
    }

    public void setParallaxTexture(int parallaxTexture) {
        this.parallaxTexture = parallaxTexture;
    }

    public int getParallaxTexture() {
        return parallaxTexture;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public void setShininess(double shininess) {
        this.shininess = (float) shininess;
    }

    public float getShininess() {
        return shininess;
    }

    public static void sendToShader(GL4 gl, Shader shader) {
        shader.setUniform(gl, "material.diffuse", 0);
        shader.setUniform(gl, "material.specular", 1);
        shader.setUniform(gl, "material.normal", 2);
        shader.setUniform(gl, "material.parallax", 3);
    }
}
