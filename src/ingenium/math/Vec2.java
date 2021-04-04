package ingenium.math;

public class Vec2 {
    private float x;
    private float y;
    private float w;

    public Vec2(float x, float y, float w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    public Vec2(float x, float y) {
        this(x, y, 1);
    }

    public Vec2(float x) {
        this(x, 0, 1);
    }

    public Vec2() {
        this(0, 0, 1);
    }

    public float len() {
        return Vec2.len(this);
    }

    public Vec2 sub(Vec2 v2) {
        this.x -= v2.x;
        this.y -= v2.y;
        return this;
    }

    public Vec2 add(Vec2 v2) {
        this.x += v2.x;
        this.y += v2.y;
        return this;
    }

    public Vec2 mul(Vec2 v2) {
        this.x *= v2.x;
        this.y *= v2.y;
        return this;
    }

    public Vec2 div(Vec2 v2) {
        this.x /= v2.x;
        this.y /= v2.y;
        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getW() {
        return w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setW(float w) {
        this.w = w;
    }

    public static Vec2 sub(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vec2 add(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vec2 mul(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x * v2.x, v1.y * v2.y);
    }

    public static Vec2 div(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x / v2.x, v1.y / v2.y);
    }

    public static Vec2 mulFloat(Vec2 v1, float num) {
        return new Vec2(v1.x * num, v1.y * num);
    }

    public static Vec2 divFloat(Vec2 v1, float num) {
        return new Vec2(v1.x / num, v1.y / num);
    }

    public static float len(Vec2 v) {
        return (float) Math.sqrt(v.x * v.x + v.y * v.y);
    }

    public static Vec2 normalize(Vec2 v) {
        float l = Vec2.len(v);
        return new Vec2(v.x / l, v.y / l);
    }
}
