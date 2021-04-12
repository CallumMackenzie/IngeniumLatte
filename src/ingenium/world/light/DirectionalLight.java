package ingenium.world.light;

import com.jogamp.opengl.GL3;

import ingenium.math.Vec3;
import ingenium.world.Shader;

public class DirectionalLight extends Light {
    private Vec3 direction;

    public DirectionalLight(Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular, float intensity) {
        super(ambient, diffuse, specular, intensity);
        this.direction = direction;
    }

    public DirectionalLight(Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular) {
        this(direction, ambient, diffuse, specular, 1.f);
    }

    public DirectionalLight(Vec3 direction, Vec3 ambient, Vec3 diffuse) {
        this(direction, ambient, diffuse, new Vec3(0.5f, 0.5f, 0.5f));
    }

    public DirectionalLight(Vec3 direction, Vec3 ambient) {
        this(direction, ambient, new Vec3(0.6f, 0.6f, 0.6f));
    }

    public DirectionalLight(Vec3 direction) {
        this(direction, new Vec3(0.1f, 0.1f, 0.1f));
    }

    public DirectionalLight() {
        this(new Vec3(0.f, -1.f, 0.f));
    }

    public void sendToShader(GL3 gl, Shader shader) {
        shader.setUniform(gl, "dirLight.direction", getDirection(), false);
        shader.setUniform(gl, "dirLight.ambient", getAmbient(), false);
        shader.setUniform(gl, "dirLight.specular", getSpecular().mulFloat(getIntensity()), false);
        shader.setUniform(gl, "dirLight.diffuse", getDiffuse().mulFloat(getIntensity()), false);
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
