package ingenium.mesh;

import com.jogamp.opengl.GL2;
import ingenium.math.Mathematics;
import ingenium.math.Vec3;
import ingenium.mesh.Triangle.Tri3D;

public class AnimatedMesh<meshType extends Mesh<?, ?>> {
    protected meshType meshes[];
    protected meshType primaryMesh;
    protected int currentFrame = 0;
    protected int startFrame = 0;
    protected int endFrame = 0;
    protected double frameTime = 1.0;
    protected long lastFrame = System.nanoTime();
    protected boolean interpolating = true;
    protected boolean interpolatingTint = true;
    protected boolean interpolatingVertecies = true;

    public AnimatedMesh(GL2 gl, meshType base) {
        primaryMesh = base;
    }

    public void checkAdvanceFrame(GL2 gl) {
        if ((System.nanoTime() - lastFrame) >= frameTime * 1000000d) {
            if (currentFrame < endFrame)
                currentFrame++;
            else
                currentFrame = startFrame;
            lastFrame = System.nanoTime();
            if (!interpolating) {
                primaryMesh = meshes[currentFrame];
            } else {
                primaryMesh.setNumVerts(meshes[currentFrame].numVerts);
                primaryMesh.setMaterial(meshes[currentFrame].material);
            }
        }

        if (interpolating) {
            int prevFrame = ((currentFrame + 1 > endFrame) ? startFrame : (currentFrame + 1));
            float f = (float) ((System.nanoTime() - lastFrame) / (frameTime * 1000000));
            if (interpolatingVertecies) {
                float[] prevData = meshes[prevFrame].getRawVertexdata(0,
                        meshes[prevFrame].numVerts * Tri3D.Vert.vertSize, 1);
                float[] currentData = meshes[currentFrame].getRawVertexdata(0,
                        meshes[currentFrame].numVerts * Tri3D.Vert.vertSize, 1);

                for (int i = 0; i < currentData.length; i++)
                    currentData[i] = (float) Mathematics.lerp(currentData[i], prevData[i], f);
                primaryMesh.setVertexRawData(gl, 0, currentData, 1);
            }

            if (interpolatingTint)
                primaryMesh.setTint(Vec3.lerp(meshes[currentFrame].getTint(), meshes[prevFrame].getTint(), f));
        }
    }

    public meshType[] getMeshes() {
        return meshes;
    }

    public meshType getPrimaryMesh() {
        return primaryMesh;
    }

    public void setMeshes(meshType[] meshes) {
        this.meshes = meshes;
    }

    public void setPrimaryMesh(meshType primaryMesh) {
        this.primaryMesh = primaryMesh;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public double getFrameTime() {
        return frameTime;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    public void setFrameTime(double frameTime) {
        this.frameTime = frameTime;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public boolean isInterpolating() {
        return interpolating;
    }

    public void setInterpolating(boolean interpolating) {
        this.interpolating = interpolating;
    }

    public boolean isInterpolatingTint() {
        return interpolatingTint;
    }

    public boolean isInterpolatingVertecies() {
        return interpolatingVertecies;
    }

    public void setInterpolatingTint(boolean interpolatingTint) {
        this.interpolatingTint = interpolatingTint;
    }

    public void setInterpolatingVertecies(boolean interpolatingVertecies) {
        this.interpolatingVertecies = interpolatingVertecies;
    }
}
