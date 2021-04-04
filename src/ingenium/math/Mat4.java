package ingenium.math;

public class Mat4 {
    float m[][] = new float[4][4];

    public static Mat4 perspective(float fovDeg, float aspectRatio, float near, float far) {
        float fovRad = 1.f / (float) Math.tan(Rotation.degToRad(fovDeg * 0.5f));
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = aspectRatio * fovRad;
        matrix.m[1][1] = fovRad;
        matrix.m[2][2] = far / (far - near);
        matrix.m[3][2] = (-far * near) / (far - near);
        matrix.m[2][3] = 1.f;
        matrix.m[3][3] = 0.f;
        return matrix;
    };

    public static Mat4 inverse(Mat4 m) {
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = m.m[0][0];
        matrix.m[0][1] = m.m[1][0];
        matrix.m[0][2] = m.m[2][0];
        matrix.m[0][3] = 0.f;
        matrix.m[1][0] = m.m[0][1];
        matrix.m[1][1] = m.m[1][1];
        matrix.m[1][2] = m.m[2][1];
        matrix.m[1][3] = 0.f;
        matrix.m[2][0] = m.m[0][2];
        matrix.m[2][1] = m.m[1][2];
        matrix.m[2][2] = m.m[2][2];
        matrix.m[2][3] = 0.f;
        matrix.m[3][0] = -(m.m[3][0] * matrix.m[0][0] + m.m[3][1] * matrix.m[1][0] + m.m[3][2] * matrix.m[2][0]);
        matrix.m[3][1] = -(m.m[3][0] * matrix.m[0][1] + m.m[3][1] * matrix.m[1][1] + m.m[3][2] * matrix.m[2][1]);
        matrix.m[3][2] = -(m.m[3][0] * matrix.m[0][2] + m.m[3][1] * matrix.m[1][2] + m.m[3][2] * matrix.m[2][2]);
        matrix.m[3][3] = 1.f;
        return matrix;
    }

    public static Mat4 identity() {
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = 1.f;
        matrix.m[1][1] = 1.f;
        matrix.m[2][2] = 1.f;
        matrix.m[3][3] = 1.f;
        return matrix;
    }

    public static Mat4 pointedAt(Vec3 pos, Vec3 target) {
        return pointedAt(pos, target, new Vec3(0, 1, 0));
    }

    public static Mat4 pointedAt(Vec3 pos, Vec3 target, Vec3 up) {
        Vec3 newForward = Vec3.sub(target, pos);
        newForward = Vec3.normalize(newForward);

        Vec3 a = Vec3.mulFloat(newForward, Vec3.dot(up, newForward));
        Vec3 newUp = Vec3.sub(up, a);
        newUp = Vec3.normalize(newUp);

        Vec3 newRight = Vec3.cross(newUp, newForward);
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = newRight.x;
        matrix.m[0][1] = newRight.y;
        matrix.m[0][2] = newRight.z;
        matrix.m[0][3] = 0.f;
        matrix.m[1][0] = newUp.x;
        matrix.m[1][1] = newUp.y;
        matrix.m[1][2] = newUp.z;
        matrix.m[1][3] = 0.f;
        matrix.m[2][0] = newForward.x;
        matrix.m[2][1] = newForward.y;
        matrix.m[2][2] = newForward.z;
        matrix.m[2][3] = 0.f;
        matrix.m[3][0] = pos.x;
        matrix.m[3][1] = pos.y;
        matrix.m[3][2] = pos.z;
        matrix.m[3][3] = 1.f;
        return matrix;
    }

    public static Mat4 scale(float x) {
        return scale(x, 1, 1);
    }

    public static Mat4 scale(float x, float y) {
        return scale(x, y, 1);
    }

    public static Mat4 scale(float x, float y, float z) {
        Mat4 matrix = Mat4.identity();
        matrix.m[0][0] = x;
        matrix.m[1][1] = y;
        matrix.m[2][2] = z;
        return matrix;
    }

    public static Mat4 translation(float x) {
        return translation(x, 0, 0);
    }

    public static Mat4 translation(float x, float y) {
        return translation(x, y, 0);
    }

    public static Mat4 translation(float x, float y, float z) {
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = 1.f;
        matrix.m[1][1] = 1.f;
        matrix.m[2][2] = 1.f;
        matrix.m[3][3] = 1.f;
        matrix.m[3][0] = x;
        matrix.m[3][1] = y;
        matrix.m[3][2] = z;
        return matrix;
    }

    public static Mat4 mul(Mat4 m1, Mat4 m2) {
        Mat4 matrix = new Mat4();
        for (int c = 0; c < 4; c++)
            for (int r = 0; r < 4; r++)
                matrix.m[r][c] = m1.m[r][0] * m2.m[0][c] + m1.m[r][1] * m2.m[1][c] + m1.m[r][2] * m2.m[2][c]
                        + m1.m[r][3] * m2.m[3][c];
        return matrix;
    }

    public static Mat4 rotationX(float xRad) {
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = 1.f;
        matrix.m[1][1] = (float) Math.cos(xRad);
        matrix.m[1][2] = (float) Math.sin(xRad);
        matrix.m[2][1] = -(float) Math.sin(xRad);
        matrix.m[2][2] = (float) Math.cos(xRad);
        matrix.m[3][3] = 1.f;
        return matrix;
    }

    public static Mat4 rotationY(float yRad) {
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = (float) Math.cos(yRad);
        matrix.m[0][2] = (float) Math.sin(yRad);
        matrix.m[2][0] = -(float) Math.sin(yRad);
        matrix.m[1][1] = 1.f;
        matrix.m[2][2] = (float) Math.cos(yRad);
        matrix.m[3][3] = 1.f;
        return matrix;
    }

    public static Mat4 rotationZ(float zRad) {
        Mat4 matrix = new Mat4();
        matrix.m[0][0] = (float) Math.cos(zRad);
        matrix.m[0][1] = (float) Math.sin(zRad);
        matrix.m[1][0] = -(float) Math.sin(zRad);
        matrix.m[1][1] = (float) Math.cos(zRad);
        matrix.m[2][2] = 1.f;
        matrix.m[3][3] = 1.f;
        return matrix;
    }

    public static Mat4 rotationOnPoint(float xRad, float yRad, float zRad, Vec3 pt) {
        Mat4 mat = Mat4.mul(
                Mat4.mul(Mat4.translation(pt.x, pt.y, pt.z),
                        Mat4.mul(Mat4.mul(Mat4.rotationX(xRad), Mat4.rotationY(yRad)), Mat4.rotationZ(zRad))),
                Mat4.translation(-pt.x, -pt.y, -pt.z));
        return mat;
    }
}
