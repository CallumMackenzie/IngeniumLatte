package ingenium;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
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
    private int clearColour = 0x000000;
    private float clearAlpha = 1.f;
    protected Frame frame;
    private FPSAnimator animator;

    public GLWindow(String name, float width, float height, GLCapabilities glcapabilities) {
        this.glcapabilities = glcapabilities;
        this.name = name;
        this.width = width;
        this.height = height;
        this.canvas = new GLCanvas(this.glcapabilities);
        frame = new Frame(this.name);
        this.aspect = height / width;

        animator = new FPSAnimator(120);
        animator.add(canvas);

        canvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
                resize(glautodrawable.getGL().getGL4(), width, height);
            }

            @Override
            public void init(GLAutoDrawable glautodrawable) {
                create(glautodrawable.getGL().getGL4());
            }

            @Override
            public void dispose(GLAutoDrawable glautodrawable) {
                close(glautodrawable.getGL().getGL4());
            }

            @Override
            public void display(GLAutoDrawable glautodrawable) {
                displayGL(glautodrawable.getGL().getGL4(), glautodrawable.getSurfaceWidth(),
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
        animator.start();
    }

    public void setClearColour(int hex, float alpha) {
        this.clearColour = hex;
        this.clearAlpha = alpha;
    }

    public void setClearColour(int hex) {
        setClearColour(hex, 1.f);
    }

    public void clear(GL4 gl) {
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
    }

    public void resize(GL4 gl, int width, int height) {
        float r = (float) ((clearColour & 0xFF0000) >> 16) / 255.f;
        float g = (float) ((clearColour & 0x00FF00) >> 8) / 255.f;
        float b = (float) ((clearColour & 0x0000FF)) / 255.f;
        gl.glClearColor(r, g, b, clearAlpha);
        gl.glClearDepth(1.f);
        gl.glViewport(0, 0, width, height);
    }

    protected void displayGL(GL4 gl, int surfaceWidth, int surfaceHeight) {
        render(gl);
    }

    protected void create(GL4 gl) {

    }

    protected void close(GL4 gl) {

    }

    protected void render(GL4 gl) {

    }

    public float getAspect() {
        return aspect;
    }

    public String getName() {
        return name;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public GLCanvas getCanvas() {
        return canvas;
    }
}
