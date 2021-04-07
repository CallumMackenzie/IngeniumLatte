import com.jogamp.opengl.GL4;

import ingenium.*;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Mesh m;
    Shader shader;
    Camera camera = new Camera(9.f / 16.f);
    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] { new PointLight(new Vec3(0, 1, 2.5)) };

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    App() {
        super("Ingneium Latte", 1600.f, 900.f);
        setClearColour(0x404040, 1);
        dLight.setDirection(new Vec3());
        p[0].setIntensity(2);
        p[0].setSpecular(new Vec3(0.1, 0.1, 0.1));
    }

    @Override
    protected void onCreate(GL4 gl) {
        shader = new Shader(gl, Utils.getFileAsString("./shaders/vert3d.vert"),
                Utils.getFileAsString("./shaders/phong.frag"));
        m = new Mesh();
        Material mtrl = m.getMaterial();
        mtrl.setShininess(0.2);
        m.setMaterial(mtrl);
        m.loadFromObjData(Utils.getFileAsString("./resource/uvsmoothnt.obj"));
        m.setTexture(gl, "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg");
        m.load(gl);
        m.setPosition(new Vec3(0f, 0f, 3f));

        shader.use(gl);
    }

    float interp = 0f;

    @Override
    protected void onRender(GL4 gl) {
        camera.stdControl(input, 0.1f);
        m.setRotation(m.getRotation().add(new Vec3(0.01f, 0.01f, 0.01f)));
        clear(gl);
        Mesh.renderAll(gl, shader, camera, dLight, new Mesh[] { m }, p);
    }

    @Override
    protected void onClose(GL4 gl) {

    }

    @Override
    protected void onUpdate() {

    }
}
