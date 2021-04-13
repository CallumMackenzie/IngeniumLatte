package ingenium;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GL2;

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
    protected void render(GL2 gl) {
        frame.requestFocus();
        onRender(gl);
    }

    @Override
    protected void create(GL2 gl) {
        onCreate(gl);
    }

    @Override
    protected void close(GL2 gl) {
        onClose(gl);
    }

    protected void onRender(GL2 gl) {

    }

    protected void onCreate(GL2 gl) {

    }

    protected void onClose(GL2 gl) {

    }

    protected void onUpdate() {

    }

    /**
     * Sets the proper states for 3D rendering
     * 
     * @param gl the GL2 object of the program
     */
    public void init3D(GL2 gl) {
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
    }

    /**
     * Sets the proper states for 2D rendering
     * 
     * @param gl the GL2 object of the program
     */
    public void init2D(GL2 gl) {
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glDepthRange(0.0, 1.0);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
    }
}
