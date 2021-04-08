import com.jogamp.opengl.GL4;

import ingenium.*;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Mesh m[] = new Mesh[100];
    Shader shader;
    Camera camera = new Camera(9.f / 16.f);
    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] { new PointLight(new Vec3(0, 1, 2.5)) };

    public static void main(String[] args) throws Exception {
        new App().start();
        Mesh.getGeometryReferenceCache().use(true);
        Mesh.getTextureReferenceCache().use(true);
        Mesh.getGeometryValueCache().use(true);
    }

    App() {
        super("Ingneium Latte", 1600.f, 900.f);
        p[0].setIntensity(2);
        p[0].setSpecular(new Vec3(0.1, 0.1, 0.1));
        time.setTargetRenderFPS(1000);
    }

    @Override
    protected void onCreate(GL4 gl) {
        setClearColour(gl, 0x404040, 1);
        shader = new Shader(gl, Utils.getFileAsString("./shaders/vert3d.vert"),
                Utils.getFileAsString("./shaders/phong.frag"));

        String objPath = "./resource/uvsmoothnt.obj";
        for (int i = 0; i < m.length; i++) {
            m[i] = new Mesh(new Vec3((i * 2) - m.length, 0, 3), new Vec3(), new Vec3(1, 1, 1), new Vec3(),
                    new Material(0.2));
            m[i].make(gl, objPath, "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg");
        }

        shader.use(gl);
    }

    @Override
    protected void onRender(GL4 gl) {
        System.out.println("FPS: " + Time.deltaTimeToFPS(time.getRenderDeltaTime()));
        camera.stdControl(input, time.getRenderDeltaTime() * 3);
        for (int i = 0; i < m.length; i++) {
            m[i].setRotation(m[i].getRotation().add(new Vec3(1f, 1f, 1f).mulFloat(time.getRenderDeltaTime())));
        }
        clear(gl);
        Mesh.renderAll(gl, shader, camera, dLight, m, p);
    }

    @Override
    protected void onClose(GL4 gl) {
        System.out.println("Geometry value cache hits: " + Mesh.getGeometryValueCache().getCacheHits());
        System.out.println("Geometry reference cache hits: " + Mesh.getGeometryReferenceCache().getCacheHits());
        System.out.println("Texture reference cache hits: " + Mesh.getTextureReferenceCache().getCacheHits());
    }

    @Override
    protected void onUpdate() {

    }
}
