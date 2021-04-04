package ingenium.mesh;

public class Material {
    private int diffuseTexture = -1;
    private int specularTexture = -1;
    private int normalTexture = -1;
    private int parallaxTexture = -1;
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

    public float getShininess() {
        return shininess;
    }
}
