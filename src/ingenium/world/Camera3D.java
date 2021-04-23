package ingenium.world;

import ingenium.math.Vec3;
import com.jogamp.opengl.GL2;
import ingenium.math.Mat4;
import ingenium.math.Rotation;

public class Camera3D extends Position<Vec3, Vec3> {
    private float FOV;
    private float clipNear;
    private float clipFar;
    private float aspect;

    /**
     * Create a new camera
     * 
     * @param aspect   the aspect ratio of the screen (height / width)
     * @param position the position
     * @param rotation the rotation
     * @param FOV      the field of view
     * @param clipNear the near clipping distance
     * @param clipFar  the far clipping distance
     */
    public Camera3D(float aspect, Vec3 position, Vec3 rotation, float FOV, float clipNear, float clipFar) {
        this.position = position;
        this.rotation = rotation;
        this.rotationPoint = new Vec3();
        this.FOV = FOV;
        this.clipNear = clipNear;
        this.clipFar = clipFar;
        this.aspect = aspect;
    }

    /**
     * Create a new camera
     * 
     * @param aspect   the aspect ratio of the screen (height / width)
     * @param position the position
     * @param rotation the rotation
     * @param FOV      the field of view
     */
    public Camera3D(float aspect, Vec3 position, Vec3 rotation, float FOV) {
        this(aspect, position, rotation, FOV, 0.1f, 500.f);
    }

    /**
     * Create a new camera
     * 
     * @param aspect   the aspect ratio of the screen (height / width)
     * @param position the position
     * @param rotation the rotation
     */
    public Camera3D(float aspect, Vec3 position, Vec3 rotation) {
        this(aspect, position, rotation, 70.f);
    }

    /**
     * Create a new camera
     * 
     * @param aspect   the aspect ratio of the screen (height / width)
     * @param position the position
     */
    public Camera3D(float aspect, Vec3 position) {
        this(aspect, position, new Vec3());
    }

    /**
     * Create a new camera
     * 
     * @param aspect the aspect ratio of the screen (height / width)
     */
    public Camera3D(float aspect) {
        this(aspect, new Vec3());
    }

    /**
     * 
     * @return a vector repersenting the direction the camera is looking
     */
    public Vec3 lookVector() {
        Vec3 target = new Vec3(0.f, 0.f, 1.f);
        Mat4 mRotation = Mat4.rotation(this.rotation);
        target = target.mulMat4(mRotation);
        return target;
    }

    /**
     * 
     * @return the perspective projection matrix of the camera
     */
    public Mat4 perspective() {
        return Mat4.perspective(this.FOV, this.aspect, this.clipNear, this.clipFar);
    }

    /**
     * 
     * @return the camera matrix
     */
    public Mat4 cameraMatrix() {
        Vec3 vUp = new Vec3(0.f, 1.f, 0.f);
        Vec3 vTarget = new Vec3(0.f, 0.f, 1.f);
        Vec3 camRot = vTarget.mulMat4(Mat4.rotationOnPoint(this.rotation, this.rotationPoint));
        vTarget = position.add(camRot);
        Mat4 matCamera = Mat4.pointedAt(position, vTarget, vUp);
        return matCamera;
    }

    /**
     * 
     * @param in        input object
     * @param deltaTime the deltaTime of the last frame
     * @param camSpeed  the speed of the camera's rotation
     * @param moveSpeed the speec the camera moves
     */
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
            speed *= 3.f;
        if (in.getKeyState(17))
            speed *= 7.f;

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

    /**
     * 
     * @param in        input object
     * @param deltaTime the deltaTime of the last frame
     */
    public void stdControl(ingenium.Input in, float deltaTime) {
        stdControl(in, deltaTime, 0.7f, 0.5f);
    }

    /**
     * 
     * @param gl     the GL2 object of the program
     * @param shader the shader to send information to
     */
    public void sendToShader(GL2 gl, Shader shader) {
        shader.setUniform(gl, Shader.Uniforms.camera3D_view, cameraMatrix().inverse());
        shader.setUniform(gl, Shader.Uniforms.camera3D_viewPos, getPosition(), false);
        shader.setUniform(gl, Shader.Uniforms.camera3D_projection, perspective());
    }

    /**
     * 
     * @return the field of view
     */
    public float getFOV() {
        return FOV;
    }

    /**
     * 
     * @return the far clip distance
     */
    public float getClipFar() {
        return clipFar;
    }

    /**
     * 
     * @return the near clip distance
     */
    public float getClipNear() {
        return clipNear;
    }

    /**
     * 
     * @param FOV the field of view to set
     */
    public void setFOV(float FOV) {
        this.FOV = FOV;
    }

    /**
     * 
     * @param clipFar the far clip distance to set
     */
    public void setClipFar(float clipFar) {
        this.clipFar = clipFar;
    }

    /**
     * 
     * @param clipNear the near clip distance to set
     */
    public void setClipNear(float clipNear) {
        this.clipNear = clipNear;
    }

    /**
     * 
     * @param aspect the aspect to set (height / width)
     */
    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    /**
     * 
     * @return the aspect ratio of the camera
     */
    public float getAspect() {
        return aspect;
    }
}