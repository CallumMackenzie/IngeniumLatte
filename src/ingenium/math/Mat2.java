package ingenium.math;

public class Mat2 {
    private float m[][] = new float[2][2];

    /**
     * 
     * @return the flattened matrix
     */
    public float[] flatten() {
        return new float[] { m[0][0], m[0][1], m[1][0], m[1][1] };
    }

    /**
     * 
     * @return the 2D float array matrix
     */
    public float[][] getM() {
        return m;
    }

    /**
     * 
     * @param m the 2D float array matrix to set
     */
    public void setM(float[][] m) {
        this.m = m;
    }

    /**
     * 
     * @param radians the clockwise rotation in radians
     * @return the 2D rotation matrix
     */
    public static Mat2 rotation(float radians) {
        Mat2 mat = new Mat2();
        mat.m[0][0] = (float) Math.cos(radians);
        mat.m[0][1] = (float) -Math.sin(radians);
        mat.m[1][0] = (float) Math.sin(radians);
        mat.m[1][1] = (float) Math.cos(radians);
        return mat;
    }
}
