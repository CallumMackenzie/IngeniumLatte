package ingenium.world.light;

import ingenium.math.Vec3;

public class PointLight extends Light {
    private Vec3 position;
    private float constant = 1f;
    private float linear = 0.09f;
    private float quadratic = 0.032f;

    public PointLight(Vec3 position, Vec3 ambient, Vec3 diffuse, Vec3 specular, float intensity) {
        super(ambient, diffuse, specular, intensity);
        this.position = position;
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
}
