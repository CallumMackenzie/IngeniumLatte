package ingenium;

public class Time {
    private float targetRenderFPS = 60;
    private float renderDeltaTime = 0.01f;
    private long lastRenderFrame = System.currentTimeMillis();

    private float targetFPS = 60;
    private float deltaTime = 0.01f;
    private long lastFrame = System.currentTimeMillis();

    public void updateRenderDeltaTime() {
        renderDeltaTime = (float) (System.currentTimeMillis() - lastRenderFrame) / 1000;
        lastRenderFrame = System.currentTimeMillis();
    }

    public void updateDeltaTime() {
        deltaTime = (float) (System.currentTimeMillis() - lastFrame) / 1000;
        lastFrame = System.currentTimeMillis();
    }

    public float getTargetFPS() {
        return targetFPS;
    }

    public void setTargetFPS(float targetFPS) {
        this.targetFPS = targetFPS;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public long getLastFrame() {
        return lastFrame;
    }

    public long getLastRenderFrame() {
        return lastRenderFrame;
    }

    public float getRenderDeltaTime() {
        return renderDeltaTime;
    }

    public float getTargetRenderFPS() {
        return targetRenderFPS;
    }

    public void setTargetRenderFPS(float targetRenderFPS) {
        this.targetRenderFPS = targetRenderFPS;
    }

    public static float deltaTimeToFPS(float deltaTime) {
        return 1 / deltaTime;
    }
}
