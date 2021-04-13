package ingenium.world;

import com.jogamp.opengl.GL2;

import ingenium.math.Mat2;
import ingenium.math.Vec2;

public class Camera2D extends Position<Vec2, Float> {
    private float aspect;

    public Camera2D(float aspect, Vec2 position, float rotation) {
        this.position = position;
        this.rotation = rotation;
        this.rotationPoint = new Vec2();
        this.aspect = aspect;
    }

    public Camera2D(float aspect, Vec2 position) {
        this(aspect, position, 0f);
    }

    public Camera2D(float aspect) {
        this(aspect, new Vec2());
    }

    public Mat2 cameraMatrix() {
        return Mat2.rotation(this.rotation);
    }

    public void sendToShader(GL2 gl, Shader shader) {
        shader.setUMat2(gl, "camera.rotation", cameraMatrix());
        shader.setUVec2(gl, "camera.translation", position);
        shader.setUniform(gl, "camera.aspect", aspect);
        shader.setUVec2(gl, "camera.rotationPoint", rotationPoint);
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public void stdControl(ingenium.Input in, float deltaTime, float speed, float rotateSpeed) {
        Vec2 move = new Vec2();
        Vec2 cLV = new Vec2(Math.sin(rotation), Math.cos(rotation));
        float rotate = 0;
        if (in.getKeyState(87)) // w
            move = move.sub(cLV);
        if (in.getKeyState(83)) // s
            move = move.add(cLV);
        if (in.getKeyState(68)) // d
            move = move.add(new Vec2(Math.sin(rotation - 1.5708f), Math.cos(rotation - 1.5708f)));
        if (in.getKeyState(65)) // a
            move = move.add(new Vec2(Math.sin(rotation + 1.5708f), Math.cos(rotation + 1.5708f)));
        if (in.getKeyState(37)) // left arrow
            rotate -= rotateSpeed;
        if (in.getKeyState(39)) // right arrow
            rotate += rotateSpeed;
        if (in.getKeyState(16)) // shift
            speed *= 4;
        position = position.add(move.normalized().mulFloat(deltaTime * speed));
        rotation = rotation + rotate * deltaTime;
    }
}
