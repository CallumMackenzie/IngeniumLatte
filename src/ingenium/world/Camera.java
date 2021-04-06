package ingenium.world;

import ingenium.math.Vec3;

import com.jogamp.opengl.GL4;

import ingenium.math.Mat4;
import ingenium.math.Rotation;

public class Camera extends Position3D {
    private float FOV;
    private float clipNear;
    private float clipFar;
    private float aspect;

    public Camera(float aspect, Vec3 position, Vec3 rotation, float FOV, float clipNear, float clipFar) {
        this.position = position;
        this.rotation = rotation;
        this.rotationPoint = new Vec3();
        this.FOV = FOV;
        this.clipNear = clipNear;
        this.clipFar = clipFar;
        this.aspect = aspect;
    }

    public Camera(float aspect, Vec3 position, Vec3 rotation, float FOV) {
        this(aspect, position, rotation, FOV, 0.1f, 500.f);
    }

    public Camera(float aspect, Vec3 position, Vec3 rotation) {
        this(aspect, position, rotation, 70.f);
    }

    public Camera(float aspect, Vec3 position) {
        this(aspect, position, new Vec3());
    }

    public Camera(float aspect) {
        this(aspect, new Vec3());
    }

    public Vec3 lookVector() {
        Vec3 target = new Vec3(0.f, 0.f, 1.f);
        Mat4 mRotation = Mat4.rotation(this.rotation);
        target = target.mulMat4(mRotation);
        return target;
    }

    public Mat4 perspective() {
        return Mat4.perspective(this.FOV, this.aspect, this.clipNear, this.clipFar);
    }

    public Mat4 cameraMatrix() {
        Vec3 vUp = new Vec3(0.f, 1.f, 0.f);
        Vec3 vTarget = new Vec3(0.f, 0.f, 1.f);
        Vec3 camRot = vTarget.mulMat4(Mat4.rotationOnPoint(this.rotation, this.rotationPoint));
        vTarget = position.add(camRot);
        Mat4 matCamera = Mat4.pointedAt(position, vTarget, vUp);
        return matCamera;
    }

    public void stdControl(ingenium.Input in, float deltaTime, float camSpeed, float moveSpeed) {
        Vec3 cLV = lookVector();
        float cameraMoveSpeed = camSpeed;
        float speed = moveSpeed;

        Vec3 forward = new Vec3();
        Vec3 up = new Vec3(0.f, 1.f, 0.f);
        Vec3 rotate = new Vec3();
        if (in.getKeyState(87)) // w
            forward = forward.add(cLV);
        if (in.getKeyState(83)) // s
            forward = forward.add(cLV.mulFloat(-1.f));
        if (in.getKeyState(68)) // d
            forward = forward.add(Vec3.cross(cLV, up));
        if (in.getKeyState(65)) // a
            forward = forward.add(Vec3.cross(cLV, up).mulFloat(-1.f));
        if (in.getKeyState(81) || in.getKeyState(' '))
            forward.setY(forward.getY() + 1.f);
        if (in.getKeyState(69))
            forward.setY(forward.getY() - 1.f);

        if (in.getKeyState(37)) // Arrow left
            rotate.setY(-cameraMoveSpeed);
        if (in.getKeyState(39)) // Arrow right
            rotate.setY(cameraMoveSpeed);
        if (in.getKeyState(38)) // Arrow up
            rotate.setX(-cameraMoveSpeed);
        if (in.getKeyState(40)) // Arrow down
            rotate.setX(cameraMoveSpeed);
        if (in.getKeyState(16))
            speed *= 5.f;

        this.rotation = this.rotation.add(rotate.mulFloat(deltaTime));
        this.position = this.position.add(forward.normalized().mulFloat(speed * deltaTime));

        if (this.rotation.getX() >= Rotation.degToRad(87.f))
            this.rotation.setX(Rotation.degToRad(87.f));
        if (this.rotation.getX() <= -Rotation.degToRad(87.f))
            this.rotation.setX(-Rotation.degToRad(87));
        if (Math.abs(this.rotation.getY()) >= Rotation.degToRad(360.f))
            this.rotation.setY(0.f);
        if (Math.abs(this.rotation.getY()) >= Rotation.degToRad(360.f))
            this.rotation.setZ(0.f);
    }

    public void stdControl (ingenium.Input in, float deltaTime) {
        stdControl(in, deltaTime, 0.7f, 0.5f);
    }

    public void sendToShader (GL4 gl, Shader shader) {
        shader.setUniform(gl, "view", cameraMatrix().inverse());
        shader.setUniform(gl, "viewPos", getPosition(), false);
        shader.setUniform(gl, "projection", perspective());
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

    public void setFOV(float fOV) {
        FOV = fOV;
    }

    public void setClipFar(float clipFar) {
        this.clipFar = clipFar;
    }

    public void setClipNear(float clipNear) {
        this.clipNear = clipNear;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return aspect;
    }
}