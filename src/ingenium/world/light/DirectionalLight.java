package ingenium.world.light;

import ingenium.math.Vec3;

public class DirectionalLight extends Light {
    private Vec3 direction;

    public DirectionalLight(Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular, float intensity) {
        super(ambient, diffuse, specular, intensity);
        this.direction = direction;
    }

    public DirectionalLight(Vec3 direction, Vec3 ambient, Vec3 diffuse, Vec3 specular) {
        this(direction, ambient, diffuse, specular, 1f);
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
        this(new Vec3(0f, -1f, 0.4f));
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
