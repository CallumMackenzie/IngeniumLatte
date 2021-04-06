package ingenium.math;

public class Mat2 {
    private float m[][] = new float[2][2];

    public float[] flatten() {
        return new float[] { m[0][0], m[0][1], m[1][0], m[1][1] };
    }

    public float[][] getM() {
        return m;
    }

    public void setM(float[][] m) {
        this.m = m;
    }

    public static Mat2 rotation(float radians) {
        Mat2 mat = new Mat2();
        mat.m[0][0] = (float) Math.cos(radians);
        mat.m[0][1] = (float) -Math.sin(radians);
        mat.m[1][0] = (float) Math.sin(radians);
        mat.m[1][1] = (float) Math.cos(radians);
        return mat;
    }
}
