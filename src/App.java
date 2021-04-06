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

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    App() {
        super("Ingneium Latte", 1600.f, 900.f);
        setClearColour(0xabcdef, 1.f);
    }

    @Override
    protected void onCreate(GL4 gl) {
        shader = new Shader(gl, Utils.getFileAsString("./shaders/vert3dpf.vert"),
                Utils.getFileAsString("./shaders/phongpf.frag"));
        m = new Mesh();
        m.setTint(new Vec3(0.5, 0.5, 0.5));
        Material mtrl = m.getMaterial();
        mtrl.setShininess(1);
        m.setMaterial(mtrl);
        m.loadFromObjData(Utils.getFileAsString("D:\\3D Models\\torusnt.obj"));
        m.load(gl);
        m.setPosition(new Vec3(0f, 0f, 3f));

        shader.use(gl);
    }

    float interp = 0f;

    @Override
    protected void onRender(GL4 gl) {
        camera.stdControl(input, 0.1f);
        m.setRotation(m.getRotation().add(new Vec3(0.01f, 0.01f, 0.01f)));
        System.out.println(camera.getPosition());
        clear(gl);
        Mesh.renderAll(gl, shader, camera, dLight, new Mesh[] { m });
    }

    @Override
    protected void onClose(GL4 gl) {

    }

    @Override
    protected void onUpdate() {

    }
}
