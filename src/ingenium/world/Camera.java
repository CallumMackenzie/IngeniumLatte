package ingenium.world;

import ingenium.math.Vec3;
import ingenium.math.Mat4;

public class Camera {
    private Vec3 position = new Vec3();
    private Vec3 rotation = new Vec3();
    private float FOV;
    private float clipNear;
    private float clipFar;

    public Camera(Vec3 position, Vec3 rotation, float FOV, float clipNear, float clipFar) {
        this.position = position;
        this.rotation = rotation;
        this.FOV = FOV;
        this.clipNear = clipNear;
        this.clipFar = clipFar;
    }

    public Camera(Vec3 position, Vec3 rotation, float FOV) {
        this(position, rotation, FOV, 0.1f, 500.f);
    }

    public Camera(Vec3 position, Vec3 rotation) {
        this(position, rotation, 70);
    }

    public Camera(Vec3 position) {
        this(position, new Vec3());
    }

    public Camera() {
        this(new Vec3());
    }

    public Vec3 lookVector() {
        Vec3 target = new Vec3(0, 0, 1);
        Mat4 mRotation = Mat4.mul(Mat4.mul(Mat4.rotationX(this.rotation.getX()), Mat4.rotationY(this.rotation.getY())),
                Mat4.rotationZ(this.rotation.getZ()));
        target = Vec3.mulMat(target, mRotation);
        return target;
    }

    public Mat4 perspective(float aspect) {
        return Mat4.perspective(this.FOV, aspect, this.clipNear, this.clipFar);
    }

    public Mat4 cameraMatrix() {
        Vec3 vUp = new Vec3(0, 1, 0);
        Vec3 vTarget = new Vec3(0, 0, 1);
        Mat4 matCameraRotY = Mat4.rotationY(this.rotation.getY());
        Mat4 matCameraRotX = Mat4.rotationX(this.rotation.getX());
        Mat4 matCameraRotZ = Mat4.rotationZ(this.rotation.getZ());
        Vec3 camRot = Vec3.mulMat(vTarget, Mat4.mul(Mat4.mul(matCameraRotX, matCameraRotY), matCameraRotZ));
        vTarget = Vec3.add(this.position, camRot);
        Mat4 matCamera = Mat4.pointedAt(this.position, vTarget, vUp);
        return matCamera;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public float getFOV() {
        return FOV;
    }

    public float getClipFar() {
        return clipFar;
    }

    public float getClipNear() {
        return clipNear;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public void setFOV(float fOV) {
        FOV = fOV;
    }

    public void setClipFar(float clipFar) {
        this.clipFar = clipFar;
    }

    public void setClipNear(float clipNear) {
        this.clipNear = clipNear;
    }
}