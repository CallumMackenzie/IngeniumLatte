import com.jogamp.opengl.GL2;

import ingenium.*;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;

public class App2D extends Ingenium {
    Shader shader2D;
    Mesh2D m[] = new Mesh2D[3];
    Camera2D camera2d = new Camera2D(9f / 16f);

    public App2D() {
        super("Ingneium Latte", 720.f, 480.f);
        time.setTargetRenderFPS(75);
    }

    @Override
    protected void onCreate(GL2 gl) {
        // init2D(gl);
        setClearColour(gl, 0x404040, 1);
        shader2D = Shader.makeDefault2DShader(gl, true);

        String textures[][] = new String[][] {
                { "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg" },
                { "./resource/paper/b.jpg", Ingenium.NO_VALUE, "./resource/paper/n.jpg" },
                { "./resource/sbrick/b.jpg", "./resource/sbrick/s.jpg", "./resource/sbrick/n.jpg" },
                { "./resource/woodp/b.jpg", Ingenium.NO_VALUE, "./resource/woodp/n.jpg" },
                { "./resource/mtrim/b.jpg", "./resource/mtrim/s.jpg", "./resource/mtrim/n.jpg" },
                { "./resource/mplate/b.jpg", "./resource/mplate/s.jpg", "./resource/mplate/n.jpg" },
                { "./resource/scrmetal/b.jpg", "./resource/scrmetal/s.jpg", "./resource/scrmetal/n.jpg" },
                { "./resource/gate/b.jpg", "./resource/gate/s.jpg", "./resource/gate/n.jpg" } };

        m[2] = new Mesh2D();
        m[2].setScale(new Vec2(1, 0.5));
        m[2].setPosition(new Vec2(1, -1));
        m[2].setRotation(2f);
        m[2].make(gl, textures[3][0]);

        m[1] = new Mesh2D();
        m[1].setScale(new Vec2(0.5, 0.5));
        m[1].setRotationPoint(new Vec2(0, 0));
        m[1].setPosition(new Vec2());
        m[1].setRotation(0f);
        m[1].make(gl, textures[0][0]);

        m[0] = new Mesh2D();
        m[0].setScale(new Vec2(5, 5));
        m[0].setRotation(0f);
        m[0].setZIndex(1);
        m[0].make(gl, textures[2][0]);
        shader2D.use(gl);
    }

    double frame = 0;

    @Override
    protected void onRender(GL2 gl) {
        // System.out.println("FPS: " + Time.deltaTimeToFPS(time.getRenderDeltaTime()));
        frame += 1 * time.getRenderDeltaTime();
        camera2d.stdControl(input, time.getDeltaTime(), 1.5f, 2.5f);
        float sinSpeed = 0.5f;
        m[0].setRotation(m[0].getRotation() + 2f * time.getDeltaTime());
        m[1].setTint(new Vec3(Functions.sinPulse(frame, sinSpeed), Functions.sinPulse(frame + 1, sinSpeed),
                Functions.sinPulse(frame + 2, sinSpeed)));
        clear(gl);
        Mesh2D.renderAll(gl, shader2D, camera2d, m);
    }

    @Override
    protected void onClose(GL2 gl) {
        System.out.println("Geometry value cache hits: " + Geometry.getValueCache().getCacheHits());
        System.out.println("Geometry reference cache hits: " + Geometry.getReferenceCache().getCacheHits());
        System.out.println("Texture reference cache hits: " + Mesh3D.getTextureReferenceCache().getCacheHits());
    }

    @Override
    protected void onUpdate() {

    }
}
