package ingenium;

import com.jogamp.common.GlueGenVersion;
import com.jogamp.common.util.VersionUtil;
import com.jogamp.nativewindow.NativeWindowVersion;
import com.jogamp.newt.NewtVersion;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GLWindow {

    private String name;
    private float width;
    private float height;
    private float aspect;
    private GLCanvas canvas;
    private GLCapabilities glcapabilities;
    private FPSAnimator animator;
    protected Frame frame;
    protected Time time = new Time();

    public GLWindow(String name, float width, float height, GLCapabilities glcapabilities) {
        this.glcapabilities = glcapabilities;
        this.name = name;
        this.width = width;
        this.height = height;
        this.canvas = new GLCanvas(this.glcapabilities);
        frame = new Frame(this.name);
        this.aspect = height / width;

        System.err.println(VersionUtil.getPlatformInfo());
        System.err.println(GlueGenVersion.getInstance());
        System.err.println(NativeWindowVersion.getInstance());
        System.err.println(JoglVersion.getInstance());
        System.err.println(NewtVersion.getInstance());

        canvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
                resize(glautodrawable.getGL().getGL2(), width, height);
            }

            @Override
            public void init(GLAutoDrawable glautodrawable) {
                createGL(glautodrawable.getGL().getGL2());
            }

            @Override
            public void dispose(GLAutoDrawable glautodrawable) {
                close(glautodrawable.getGL().getGL2());
            }

            @Override
            public void display(GLAutoDrawable glautodrawable) {
                displayGL(glautodrawable.getGL().getGL2(), glautodrawable.getSurfaceWidth(),
                        glautodrawable.getSurfaceHeight());
            }
        });

        frame.add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent) {
                frame.remove(canvas);
                frame.dispose();
                System.exit(0);
            }
        });

        frame.setSize((int) width, (int) height);
        frame.setVisible(true);
    }

    public void start() {
        animator = new FPSAnimator((int) (time.getTargetRenderFPS() + 0.5f));
        animator.add(canvas);
        animator.start();
    }

    public void setClearColour(GL gl, int hex, float alpha) {
        float r = (float) ((hex & 0xFF0000) >> 16) / 255.f;
        float g = (float) ((hex & 0x00FF00) >> 8) / 255.f;
        float b = (float) ((hex & 0x0000FF)) / 255.f;
        gl.glClearColor(r, g, b, alpha);
        gl.glClearDepth(1.f);
    }

    public void setClearColour(GL gl, int hex) {
        setClearColour(gl, hex, 1.f);
    }

    public void clear(GL gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    }

    public void resize(GL gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    private void displayGL(GL2 gl, int surfaceWidth, int surfaceHeight) {
        time.updateRenderDeltaTime();
        render(gl);
    }

    private void createGL(GL2 gl) {
        create(gl);
        time.updateRenderDeltaTime();
    }

    /**
     * Called on program initialization
     * 
     * @param gl the GL2 object of the program
     */
    protected void create(GL2 gl) {

    }

    /**
     * Called just before program termination
     * 
     * @param gl the GL2 object of the program
     */
    protected void close(GL2 gl) {

    }

    /**
     * Called every frame
     * 
     * @param gl the GL2 object of the program
     */
    protected void render(GL2 gl) {

    }

    /**
     * 
     * @return the aspect ratio
     */
    public float getAspect() {
        return aspect;
    }

    /**
     * 
     * @return the window name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return the window height
     */
    public float getHeight() {
        return height;
    }

    /**
     * 
     * @return the window width
     */
    public float getWidth() {
        return width;
    }

    /**
     * 
     * @return the GLCanvas object of the window
     */
    public GLCanvas getCanvas() {
        return canvas;
    }
}
