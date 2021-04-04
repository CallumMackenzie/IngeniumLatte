package ingenium.mesh;

import ingenium.math.*;

public class Mesh {
    public Vec3 position;
    public Vec3 rotation;
    public Vec3 rotationCenter;
    public Vec3 scale;
    public Vec3 tint;
    public Material material;

    private boolean loaded = false;
    private float data[];
    private int mVBO = -1;
    private int mVAO = -1;
    private int mTVBO = -1;

    Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationCenter, Material material) {
        this.rotation = rotation;
        this.rotationCenter = rotationCenter;
        this.position = position;
        this.scale = scale;
        this.material = material;
    }

    Mesh(Vec3 position, Vec3 rotation, Vec3 scale, Vec3 rotationCenter) {
        this(position, rotation, rotationCenter, scale, new Material());
    }

    Mesh(Vec3 position, Vec3 rotation, Vec3 scale) {
        this(position, rotation, scale, new Vec3());
    }

    Mesh(Vec3 position, Vec3 rotation) {
        this(position, rotation, new Vec3(1, 1, 1));
    }

    Mesh(Vec3 position) {
        this(position, new Vec3());
    }

    Mesh() {
        this(new Vec3());
    }

    public int getmTVBO() {
        return mTVBO;
    }

    public int getmVAO() {
        return mVAO;
    }

    public int getmVBO() {
        return mVBO;
    }

    public float[] getData() {
        return data;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
