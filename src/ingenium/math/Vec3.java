package ingenium.math;

public class Vec3 {
    public float x;
    public float y;
    public float z;
    public float w;

    public static Vec3 sub(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vec3 add(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vec3 mul(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    public static Vec3 div(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
    }

    public static Vec3 mulFloat(Vec3 v1, float num) {
        return new Vec3(v1.x * num, v1.y * num, v1.z * num);
    }

    public static Vec3 divFloat(Vec3 v1, float num) {
        return new Vec3(v1.x / num, v1.y / num, v1.z / num);
    }

    public static float dot(Vec3 v1, Vec3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static float len(Vec3 v) {
        return (float) Math.sqrt(Vec3.dot(v, v));
    }

    public static Vec3 normalize(Vec3 v) {
        float l = Vec3.len(v);
        if (l != 0.f)
            return new Vec3(v.x / l, v.y / l, v.z / l);
        return new Vec3();
    }

    public static Vec3 cross(Vec3 v1, Vec3 v2) {
        Vec3 v = new Vec3();
        v.x = v1.y * v2.z - v1.z * v2.y;
        v.y = v1.z * v2.x - v1.x * v2.z;
        v.z = v1.x * v2.y - v1.y * v2.x;
        return v;
    }

    public static Vec3 mulMat(Vec3 i, Mat4 m) {
        Vec3 v = new Vec3();
        v.x = i.x * m.m[0][0] + i.y * m.m[1][0] + i.z * m.m[2][0] + i.w * m.m[3][0];
        v.y = i.x * m.m[0][1] + i.y * m.m[1][1] + i.z * m.m[2][1] + i.w * m.m[3][1];
        v.z = i.x * m.m[0][2] + i.y * m.m[1][2] + i.z * m.m[2][2] + i.w * m.m[3][2];
        v.w = i.x * m.m[0][3] + i.y * m.m[1][3] + i.z * m.m[2][3] + i.w * m.m[3][3];
        return v;
    }

    public Vec3(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec3(float x, float y, float z) {
        this(x, y, z, 1);
    }

    public Vec3(float x, float y) {
        this(x, y, 0, 1);
    }

    public Vec3(float x) {
        this(x, 0, 0, 1);
    }

    public Vec3() {
        this(0, 0, 0, 1);
    }

    public Vec3 add(Vec3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vec3 sub(Vec3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vec3 mul(Vec3 v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vec3 div(Vec3 v) {
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vec3 mulFloat(float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        return this;
    }

    public Vec3 addFloat(float n) {
        this.x += n;
        this.y += n;
        this.z += n;
        return this;
    }

    public Vec3 subFloat(float n) {
        this.x -= n;
        this.y -= n;
        this.z -= n;
        return this;
    }

    public Vec3 divFloat(float n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
        return this;
    }
}
