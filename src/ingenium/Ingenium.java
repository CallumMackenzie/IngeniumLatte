package ingenium;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GL4;

public class Ingenium extends GLWindow {
    public static final String NO_VALUE = "NO_VALUE";
    protected Input input = new Input();

    /**
     * 
     * @param name   the window name
     * @param width  the window width
     * @param height the window height
     */
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
        onCreate(gl);
    }

    @Override
    protected void close(GL4 gl) {
        onClose(gl);
    }

    protected void onRender(GL4 gl) {

    }

    protected void onCreate(GL4 gl) {

    }

    protected void onClose(GL4 gl) {

    }

    protected void onUpdate() {

    }

    /**
     * Sets the proper states for 3D rendering
     * 
     * @param gl the GL4 object of the program
     */
    public void init3D(GL4 gl) {
        gl.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL4.GL_BLEND);
        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL4.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        gl.glEnable(GL4.GL_CULL_FACE);
        gl.glCullFace(GL4.GL_BACK);
    }

    /**
     * Sets the proper states for 2D rendering
     * 
     * @param gl the GL4 object of the program
     */
    public void init2D(GL4 gl) {
        gl.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL4.GL_BLEND);
        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL4.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        gl.glEnable(GL4.GL_CULL_FACE);
        gl.glCullFace(GL4.GL_BACK);
    }
}
