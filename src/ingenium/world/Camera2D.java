package ingenium.world;

import com.jogamp.opengl.GL4;

import ingenium.math.Mat2;
import ingenium.math.Vec2;

public class Camera2D extends Position<Vec2, Float> {
    private float aspect;

    public Camera2D(float aspect, Vec2 position, float rotation) {
        this.position = position;
        this.rotation = rotation;
        this.rotationPoint = new Vec2();
    }

    public Camera2D(float aspect, Vec2 position) {
        this(aspect, position, 0f);
    }

    public Camera2D(float aspect) {
        this(aspect, new Vec2());
    }

    public Mat2 cameraMatrix() {
        // TODO: make actual camera matrix
        return new Mat2();
    }

    public void sendToShader(GL4 gl, Shader shader) {
        shader.setUMat2(gl, "camera", cameraMatrix());
        shader.setUniform(gl, "aspect", aspect);
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }
}
