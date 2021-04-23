package ingenium.world.light;

import com.jogamp.opengl.GL2;

import ingenium.math.Vec3;
import ingenium.world.Shader;

public class PointLight extends Light {
    private static int numPointLights = 0;

    private Vec3 position = new Vec3();
    private float constant = 1f;
    private float linear = 0.09f;
    private float quadratic = 0.032f;

    public PointLight(Vec3 position, Vec3 ambient, Vec3 diffuse, Vec3 specular, float intensity) {
        super(ambient, diffuse, specular, intensity);
        this.position = position;
        numPointLights++;
    }

    public PointLight(Vec3 position, Vec3 ambient, Vec3 diffuse, Vec3 specular) {
        this(position, ambient, diffuse, specular, 1f);
    }

    public PointLight(Vec3 position, Vec3 ambient, Vec3 diffuse) {
        this(position, ambient, diffuse, new Vec3(0.5f, 0.5f, 0.5f));
    }

    public PointLight(Vec3 position, Vec3 ambient) {
        this(position, ambient, new Vec3(0.6f, 0.6f, 0.6f));
    }

    public PointLight(Vec3 position) {
        this(position, new Vec3(0.1f, 0.1f, 0.1f));
    }

    public PointLight() {
        this(new Vec3());
    }

    public void sendToShader(GL2 gl, Shader shader, int index) {
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_position,
                getPosition(), false);
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_ambient,
                getAmbient(), false);
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_diffuse,
                getDiffuse().mulFloat(getIntensity()), false);
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_specular,
                getSpecular().mulFloat(getIntensity()), false);
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_constant,
                getConstant());
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_linear,
                getLinear());
        shader.setUniform(gl,
                Shader.Uniforms.pointLight_structName + "[" + index + "]." + Shader.Uniforms.pointLight_quadratic,
                getQuadratic());
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public float getConstant() {
        return constant;
    }

    public float getLinear() {
        return linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }

    public static int getNumPointLights() {
        return numPointLights;
    }
}
