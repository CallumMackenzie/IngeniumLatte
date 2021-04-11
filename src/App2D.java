import com.jogamp.opengl.GL4;

import ingenium.*;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.utilities.FileUtils;
import ingenium.world.*;

public class App2D extends Ingenium {
    Shader shader2D;
    Mesh2D m[] = new Mesh2D[0];
    Camera2D camera2d = new Camera2D(9f / 16f);

    public App2D() {
        super("Ingneium Latte", 1600.f, 900.f);
        time.setTargetRenderFPS(75);
    }

    @Override
    protected void onCreate(GL4 gl) {
        init2D(gl);
        setClearColour(gl, 0x404040, 1);
        shader2D = new Shader(gl, FileUtils.getFileAsString("./shaders/2D/vert2d.vert"),
                FileUtils.getFileAsString("./shaders/2D/default.frag"));

        String textures[][] = new String[][] {
                { "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg" },
                { "./resource/paper/b.jpg", Ingenium.NO_VALUE, "./resource/paper/n.jpg" },
                { "./resource/sbrick/b.jpg", "./resource/sbrick/s.jpg", "./resource/sbrick/n.jpg" },
                { "./resource/woodp/b.jpg", Ingenium.NO_VALUE, "./resource/woodp/n.jpg" },
                { "./resource/mtrim/b.jpg", "./resource/mtrim/s.jpg", "./resource/mtrim/n.jpg" },
                { "./resource/mplate/b.jpg", "./resource/mplate/s.jpg", "./resource/mplate/n.jpg" },
                { "./resource/scrmetal/b.jpg", "./resource/scrmetal/s.jpg", "./resource/scrmetal/n.jpg" },
                { "./resource/gate/b.jpg", "./resource/gate/s.jpg", "./resource/gate/n.jpg" } };

        shader2D.use(gl);
    }

    double frame = 0;

    @Override
    protected void onRender(GL4 gl) {
        // System.out.println("FPS: " + Time.deltaTimeToFPS(time.getRenderDeltaTime()));
        clear(gl);
        Mesh2D.renderAll(gl, shader2D, camera2d, m);
    }

    @Override
    protected void onClose(GL4 gl) {
        System.out.println("Geometry value cache hits: " + Geometry.getValueCache().getCacheHits());
        System.out.println("Geometry reference cache hits: " + Geometry.getReferenceCache().getCacheHits());
        System.out.println("Texture reference cache hits: " + Mesh3D.getTextureReferenceCache().getCacheHits());
    }

    @Override
    protected void onUpdate() {

    }
}
