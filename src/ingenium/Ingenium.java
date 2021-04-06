package ingenium;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GL4;

public class Ingenium extends GLWindow {

    protected Input input = new Input();

    public Ingenium(String name, float width, float height) {
        super(name, width, height, new GLCapabilities(GLProfile.getDefault()));

        frame.addKeyListener(input);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
    }

    @Override
    protected void render(GL4 gl) {
        frame.requestFocus();
        onRender(gl);
    }

    @Override
    protected void create(GL4 gl) {
        gl.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL4.GL_BLEND);
        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL4.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        onCreate(gl);
    }

    @Override
    protected void close(GL4 gl) {

    }

    protected void onRender(GL4 gl) {

    }

    protected void onCreate(GL4 gl) {

    }

    protected void onClose(GL4 gl) {

    }

    protected void onUpdate() {

    }
}
