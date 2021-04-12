package ingenium;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GL3;

public class Ingenium extends GLWindow {
    public static final String NO_VALUE = "NO_VALUE";
    public static final int NO_INT_VALUE = -1;
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
    protected void render(GL3 gl) {
        frame.requestFocus();
        onRender(gl);
    }

    @Override
    protected void create(GL3 gl) {
        onCreate(gl);
    }

    @Override
    protected void close(GL3 gl) {
        onClose(gl);
    }

    protected void onRender(GL3 gl) {

    }

    protected void onCreate(GL3 gl) {

    }

    protected void onClose(GL3 gl) {

    }

    protected void onUpdate() {

    }

    /**
     * Sets the proper states for 3D rendering
     * 
     * @param gl the GL3 object of the program
     */
    public void init3D(GL3 gl) {
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL3.GL_BLEND);
        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        gl.glEnable(GL3.GL_CULL_FACE);
        gl.glCullFace(GL3.GL_BACK);
    }

    /**
     * Sets the proper states for 2D rendering
     * 
     * @param gl the GL3 object of the program
     */
    public void init2D(GL3 gl) {
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL3.GL_BLEND);
        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        gl.glEnable(GL3.GL_CULL_FACE);
        gl.glCullFace(GL3.GL_BACK);
    }
}
