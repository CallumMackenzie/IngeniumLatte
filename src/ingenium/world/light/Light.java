package ingenium.world.light;

import ingenium.math.Vec3;

public class Light {
    private Vec3 ambient;
    private Vec3 diffuse;
    private Vec3 specular;
    private float intensity;

    public Light(Vec3 ambient, Vec3 diffuse, Vec3 specular, float intensity) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.intensity = intensity;
    }

    public Light(Vec3 ambient, Vec3 diffuse, Vec3 specular) {
        this(ambient, diffuse, specular, 1.f);
    }

    public Light(Vec3 ambient, Vec3 diffuse) {
        this(ambient, diffuse, new Vec3(0.5f, 0.5f, 0.5f));
    }

    public Light(Vec3 ambient) {
        this(ambient, new Vec3(0.6f, 0.6f, 0.6f));
    }

    public Light() {
        this(new Vec3(0.1f, 0.1f, 0.1f));
    }

    public Vec3 getAmbient() {
        return ambient;
    }

    public Vec3 getDiffuse() {
        return diffuse;
    }

    public Vec3 getSpecular() {
        return specular;
    }

    public void setAmbient(Vec3 ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(Vec3 diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(Vec3 specular) {
        this.specular = specular;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
