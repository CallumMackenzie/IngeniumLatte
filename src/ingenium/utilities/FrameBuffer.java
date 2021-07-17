package ingenium.utilities;

import com.jogamp.opengl.GL2;
import com.jogamp.common.nio.Buffers;

public class FrameBuffer {
    protected int FBO;
    protected int RBO;

    public int getFBO() {
        return FBO;
    }

    public int getRBO() {
        return RBO;
    }

    public FrameBuffer(GL2 gl) {
        int buffers[] = new int[2];
        gl.glGenFramebuffers(1, buffers, 0);
        gl.glGenRenderbuffers(1, buffers, 1);
        this.FBO = buffers[0];
        this.RBO = buffers[1];
    }

    public void delete(GL2 gl) {
        gl.glDeleteFramebuffers(1, Buffers.newDirectIntBuffer(new int[] { FBO }));
        gl.glDeleteRenderbuffers(1, Buffers.newDirectIntBuffer(new int[] { RBO }));
        FBO = GL2.GL_NONE;
        RBO = GL2.GL_NONE;
    }

    public void bind(GL2 gl) {
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, this.FBO);
        gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, this.RBO);
    }

    static void bindDefault(GL2 gl) {
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
    }
}
