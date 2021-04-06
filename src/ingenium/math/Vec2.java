package ingenium.math;

public class Vec2 extends Vec {

    /**
     * 
     * @param x the x component
     * @param y the y component
     * @param w the w component
     */
    public Vec2(float x, float y, float w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    /**
     * 
     * @param x the x component
     * @param y the y component
     */
    public Vec2(float x, float y) {
        this(x, y, 1.f);
    }

    /**
     * 
     * @param x the x component
     */
    public Vec2(float x) {
        this(x, 0.f, 1.f);
    }

    /**
     * @return a Vec2 with the values x = 0, y = 0, w = 1
     */
    public Vec2() {
        this(0, 0, 1.f);
    }

    /**
     * 
     * @param v the vector to copy
     */
    public Vec2(Vec2 v) {
        this(v.x, v.y, v.w);
    }

    /**
     * Creates a new Vec2 casting input doubles to floats
     * 
     * @param x the x component
     * @param y the y component
     * @param w the w component
     */
    public Vec2(double x, double y, double w) {
        this((float) x, (float) y, (float) w);
    }

    /**
     * Creates a new Vec2 casting input doubles to floats
     * 
     * @param x the x component
     * @param y the y component
     */
    public Vec2(double x, double y) {
        this((float) x, (float) y);
    }

    /**
     * Creates a new Vec2 casting input doubles to floats
     * 
     * @param x the x component
     */
    public Vec2(double x) {
        this((float) x);
    }

    /**
     * 
     * @return the length of the Vec2
     */
    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 
     * @param vs any number of vectors to subtract
     * @return a copy of the Vec2 with the passed values subtracted from it
     */
    public Vec2 sub(Vec2... vs) {
        Vec2 vec = new Vec2(this);
        for (Vec2 v : vs) {
            vec.x -= v.x;
            vec.y -= v.y;
        }
        return vec;
    }

    /**
     * 
     * @param vs any number of vectors to subtract
     * @return a copy of the Vec2 with the passed values added to it
     */
    public Vec2 add(Vec2... vs) {
        Vec2 vec = new Vec2(this);
        for (Vec2 v : vs) {
            vec.x += v.x;
            vec.y += v.y;
        }
        return vec;
    }

    /**
     * 
     * @param vs any number of vectors to subtract
     * @return a copy of the Vec2 multiplied by the passed values
     */
    public Vec2 mul(Vec2... vs) {
        Vec2 vec = new Vec2(this);
        for (Vec2 v : vs) {
            vec.x *= v.x;
            vec.y *= v.y;
        }
        return vec;
    }

    /**
     * 
     * @param vs any number of vectors to subtract
     * @return a copy of the Vec2 divided by the passed values
     */
    public Vec2 div(Vec2... vs) {
        Vec2 vec = new Vec2(this);
        for (Vec2 v : vs) {
            if (v.x != 0)
                vec.x /= v.x;
            if (v.y != 0)
                vec.y /= v.y;
        }
        return vec;
    }

    /**
     * 
     * Normalizes the Vec2
     */
    public void normalize() {
        float l = len();
        if (l == 0)
            return;
        this.x /= l;
        this.y /= l;
    }

    /**
     * 
     * @return a copy of the Vec2 normalized
     */
    public Vec2 normalized() {
        float l = len();
        if (l == 0)
            l = 1.f;
        return new Vec2(x / l, y / l, w);
    }

    /**
     * 
     * @param mat the matrix to multiply by
     * @return the product
     */
    public Vec2 mulMat2(Mat2 mat) {
        return mulMat2(this, mat);
    }

    @Override
    public String toString() {
        return "Vec2(" + x + "," + y + ")";
    }

    /**
     * 
     * @param vec the base vector
     * @param mat the matrix to multiply by
     * @return the product
     */
    public static Vec2 mulMat2(Vec2 vec, Mat2 mat) {
        Vec2 v = new Vec2(vec);
        v.x = (v.x * mat.getM()[0][0]) + (v.y * mat.getM()[0][1]);
        v.y = (v.x * mat.getM()[1][0]) + (v.y * mat.getM()[1][1]);
        return v;
    }
}