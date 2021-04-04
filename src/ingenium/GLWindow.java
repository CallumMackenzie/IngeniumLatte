package ingenium;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GLWindow {
    private String name;
    private int width;
    private int height;
    private float aspect;
    private GLCanvas canvas;
    private GLCapabilities glcapabilities;
    protected Frame frame;
    private int clearColour = 0x000000;
    private float clearAlpha = 1;

    public GLWindow(String name, int width, int height, GLCapabilities glcapabilities) {
        this.glcapabilities = glcapabilities;
        this.name = name;
        this.width = width;
        this.height = height;
        this.canvas = new GLCanvas(this.glcapabilities);
        frame = new Frame(this.name);
        this.aspect = height / width;

        canvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
                resize(glautodrawable.getGL().getGL2(), width, height);
            }

            @Override
            public void init(GLAutoDrawable glautodrawable) {
                create(glautodrawable.getGL().getGL2());
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

        frame.setSize(width, height);
        frame.setVisible(true);
    }

    protected void create(GL2 gl) {

    }

    protected void update() {
    }

    protected void fixedUpdate() {
    }

    protected void close(GL2 gl) {
    }

    public void setClearColour(int hex, float alpha) {
        this.clearColour = hex;
        this.clearAlpha = alpha;
    }

    public void setClearColour(int hex) {
        setClearColour(hex, 1);
    }

    public void resize(GL2 gl, int width, int height) {
        float r =  (float) ((clearColour & 0xFF0000) >> 16) / 255.f;
        float g =  (float) ((clearColour & 0x00FF00) >> 8) / 255.f;
        float b =  (float) ((clearColour & 0x0000FF)) / 255.f;
        gl.glClearColor(r, g, b, clearAlpha);
        gl.glClearDepth(1);
        gl.glViewport(0, 0, width, height);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    }

    protected void displayGL(GL2 gl, int surfaceWidth, int surfaceHeight) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        onRender(gl);
    }

    protected void onRender (GL2 gl) {

    }

    public float getAspect() {
        return aspect;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public GLCanvas getCanvas() {
        return canvas;
    }
}
