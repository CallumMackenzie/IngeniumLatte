package ingenium.math;

/**
 * Repersents a column vector [ x y z w ]
 */
public class Vec3 extends Vec {
    protected float z;

    /**
     * 
     * @param x x component of Vec3
     * @param y y component of Vec3
     * @param z z component of Vec3
     * @param w w component of Vec3
     */
    public Vec3(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * 
     * @param x x component of Vec3
     * @param y y component of Vec3
     * @param z z component of Vec3
     */
    public Vec3(float x, float y, float z) {
        this(x, y, z, 1.f);
    }

    /**
     * 
     * @param x x component of Vec3
     * @param y y component of Vec3
     */
    public Vec3(float x, float y) {
        this(x, y, 0.f, 1.f);
    }

    /**
     * 
     * @param x x component of Vec3
     */
    public Vec3(float x) {
        this(x, 0.f, 0.f, 1.f);
    }

    /**
     * @return a Vec3 with the values x, y, z = 0, w = 1
     */
    public Vec3() {
        this(0.f, 0.f, 0.f, 1.f);
    }

    /**
     * 
     * @param v the vector to copy
     */
    public Vec3(Vec3 v) {
        this(v.x, v.y, v.z, v.w);
    }

    /**
     * Creates a new Vec3 casting input doubles to floats
     * 
     * @param x the x component
     * @param y the y component
     * @param z the z component
     * @param w the w component
     */
    public Vec3(double x, double y, double z, double w) {
        this((float) x, (float) y, (float) z, (float) w);
    }

    /**
     * Creates a new Vec3 casting input doubles to floats
     * 
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public Vec3(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a new Vec3 casting input doubles to floats
     * 
     * @param x the x component
     * @param y the y component
     */
    public Vec3(double x, double y) {
        this((float) x, (float) y);
    }

    /**
     * Creates a new Vec3 casting input doubles to floats
     * 
     * @param x the x component
     */
    public Vec3(double x) {
        this((float) x);
    }

    /**
     * 
     * @return the z component of the Vec3
     */
    public float getZ() {
        return z;
    }

    /**
     * 
     * @param z the z value to set
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * 
     * @param z the z value to set
     */
    public void setZ(double z) {
        this.z = (float) z;
    }

    public Vec2 toVec2() {
        return new Vec2(x, y, w);
    }

    /**
     * 
     * @param m the Mat4 to multiply the vector by
     * @return a new vector with the multiplication result
     */
    public Vec3 mulMat4(Mat4 m) {
        Vec3 v = new Vec3();
        v.x = x * m.getM()[0][0] + y * m.getM()[1][0] + z * m.getM()[2][0] + w * m.getM()[3][0];
        v.y = x * m.getM()[0][1] + y * m.getM()[1][1] + z * m.getM()[2][1] + w * m.getM()[3][1];
        v.z = x * m.getM()[0][2] + y * m.getM()[1][2] + z * m.getM()[2][2] + w * m.getM()[3][2];
        v.w = x * m.getM()[0][3] + y * m.getM()[1][3] + z * m.getM()[2][3] + w * m.getM()[3][3];
        return v;
    }

    /**
     * 
     * @param vs any number of vectors to add
     * @return a new Vec3 with the sum
     */
    public Vec3 add(Vec3... vs) {
        Vec3 vec = new Vec3(this);
        for (Vec3 v : vs) {
            vec.x += v.x;
            vec.y += v.y;
            vec.z += v.z;
        }
        return vec;
    }

    /**
     * 
     * @param vs any number of vectors to subtract
     * @return a new Vec3 with the difference
     */
    public Vec3 sub(Vec3... vs) {
        Vec3 vec = new Vec3(this);
        for (Vec3 v : vs) {
            vec.x -= v.x;
            vec.y -= v.y;
            vec.z -= v.z;
        }
        return vec;
    }

    /**
     * 
     * @param vs any number of vectors to multiply
     * @return a new Vec3 with the product
     */
    public Vec3 mul(Vec3... vs) {
        Vec3 vec = new Vec3(this);
        for (Vec3 v : vs) {
            vec.x *= v.x;
            vec.y *= v.y;
            vec.z *= v.z;
        }
        return vec;
    }

    /**
     * 
     * @param vs any number of vectors to divide
     * @return a new Vec3 with the quotient
     */
    public Vec3 div(Vec3... vs) {
        Vec3 vec = new Vec3(this);
        for (Vec3 v : vs) {
            vec.x /= v.x;
            vec.y /= v.y;
            vec.z /= v.z;
        }
        return vec;
    }

    /**
     * 
     * @param ns floats to multiply
     * @return a new Vec3 with the product
     */
    public Vec3 mulFloat(float... ns) {
        Vec3 vec = new Vec3(this);
        for (float n : ns) {
            vec.x *= n;
            vec.y *= n;
            vec.z *= n;
        }
        return vec;
    }

    /**
     * 
     * @param ns floats to add
     * @return a new Vec3 with the sum
     */
    public Vec3 addFloat(float... ns) {
        Vec3 vec = new Vec3(this);
        for (float n : ns) {
            vec.x += n;
            vec.y += n;
            vec.z += n;
        }
        return vec;
    }

    /**
     * 
     * @param ns floats to multiply
     * @return a new Vec3 with the difference
     */
    public Vec3 subFloat(float... ns) {
        Vec3 vec = new Vec3(this);
        for (float n : ns) {
            vec.x -= n;
            vec.y -= n;
            vec.z -= n;
        }
        return vec;
    }

    /**
     * 
     * @param ns floats to multiply
     * @return a new Vec3 with the product
     */
    public Vec3 divFloat(float... ns) {
        Vec3 vec = new Vec3(this);
        for (float n : ns) {
            vec.x /= n;
            vec.y /= n;
            vec.z /= n;
        }
        return vec;
    }

    /**
     * 
     * @return a copy of the Vec3 normalized
     */
    public Vec3 normalized() {
        Vec3 vec = new Vec3(this);
        float l = vec.len();
        if (l != 0.f) {
            vec.x /= l;
            vec.y /= l;
            vec.z /= l;
        }
        return vec;
    }

    /**
     * 
     * Normalizes the Vec3
     */
    public void normalize() {
        float l = len();
        if (l != 0.f) {
            this.x /= l;
            this.y /= l;
            this.z /= l;
        }
    }

    /**
     * 
     * @param v the vector to compare
     * @return whether the x, y, and z components are equal
     */
    public boolean equalsXYZ(Vec3 v) {
        return (v.x == this.x && v.y == this.y && v.z == this.z);
    }

    /**
     * 
     * @param v the vector to compare
     * @return whether the x, y, z, and w components are equal
     */
    public boolean equalsXYZW(Vec3 v) {
        return (v.x == this.x && v.y == this.y && v.z == this.z && v.w == this.w);
    }

    /**
     * @return the length of the Vec3
     */
    public float len() {
        return (float) Math.sqrt(Vec3.dot(this, this));
    }

    public int hashCode() {
        return ("Vec3(" + x + "," + y + "," + z + "," + w + ")").hashCode();
    }

    @Override
    public String toString() {
        return "Vec3(" + x + "," + y + "," + z + ")";
    }

    /**
     * 
     * @param v1 the first Vec3
     * @param v2 the second Vec3
     * @return the dot product
     */
    public static float dot(Vec3 v1, Vec3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    /**
     * 
     * @param v1 the first Vec3
     * @param v2 the second Vec3
     * @return the cross product
     */
    public static Vec3 cross(Vec3 v1, Vec3 v2) {
        Vec3 v = new Vec3();
        v.x = v1.y * v2.z - v1.z * v2.y;
        v.y = v1.z * v2.x - v1.x * v2.z;
        v.z = v1.x * v2.y - v1.y * v2.x;
        return v;
    }

    public static Vec3 lerp (Vec3 a, Vec3 b, float t) {
        return new Vec3(Mathematics.lerp(a.x, b.x, t), Mathematics.lerp(a.y, b.y, t), Mathematics.lerp(a.z, b.z, t));
    }
}
