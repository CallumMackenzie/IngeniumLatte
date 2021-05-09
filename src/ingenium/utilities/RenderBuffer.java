package ingenium.utilities;

import com.jogamp.opengl.GL2;

import ingenium.Ingenium;
import ingenium.math.Vec2;
import ingenium.mesh.Mesh2D;

public class RenderBuffer extends FrameBuffer {
    private int width = 0;
    private int height = 0;
    private int textures[] = new int[0];

    public RenderBuffer(GL2 gl) {
        super(gl);
    }

    public void addTexture(GL2 gl, int width, int height, int slot, int minFilter, int magFilter) {
        gl.glActiveTexture(slot);
        this.bind(gl);
        int texs[] = new int[textures.length + 1];
        gl.glGenTextures(1, texs, texs.length - 1);
        if (textures.length > 0)
            System.arraycopy(textures, 0, texs, textures.length, 1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texs[0]);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, magFilter);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, texs[0], 0);
        textures = texs;
    }

    public void addTexture(GL2 gl, int width, int height, int slot) {
        addTexture(gl, width, height, slot, GL2.GL_LINEAR, GL2.GL_LINEAR);
    }

    public void addTexture(GL2 gl, int width, int height) {
        addTexture(gl, width, height, GL2.GL_TEXTURE0);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getTextures() {
        return textures;
    }

    public Mesh2D createRenderQuad (GL2 gl) {
        Mesh2D m = new Mesh2D();
        m.make(gl, Ingenium.NO_VALUE);
        m.getMaterial().setDiffuseTexture(this.getTextures()[0]);
        m.setScale(new Vec2((float) width / (float) height, 1));
        return m;
    }

    public static RenderBuffer createRenderTexture(GL2 gl, int width, int height) {
        RenderBuffer fb = new RenderBuffer(gl);
        fb.bind(gl);
        fb.width = width;
        fb.height = height;
        gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_DEPTH24_STENCIL8, fb.width, fb.height);
        gl.glFramebufferRenderbuffer(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_STENCIL_ATTACHMENT, GL2.GL_RENDERBUFFER, fb.RBO);
        fb.addTexture(gl, fb.width, fb.height);
        FrameBuffer.bindDefault(gl);
        gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);
        return fb;
    }

    public static void renderToRenderTexture(GL2 gl, RenderBuffer fb) {
        fb.bind(gl);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glViewport(0, 0, fb.width, fb.height);
    }

    public static void setDefaultRenderBuffer(GL2 gl, ingenium.GLWindow window) {
        FrameBuffer.bindDefault(gl);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glViewport(0, 0, (int) window.getWidth(), (int) window.getHeight());
    }
}
