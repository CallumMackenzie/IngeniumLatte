import com.jogamp.opengl.GL4;

import ingenium.*;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Mesh m[] = new Mesh[500];
    Shader shader;
    Camera camera = new Camera(9.f / 16.f);
    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] { new PointLight(new Vec3(0, 1, 2.5)) };

    public static void main(String[] args) throws Exception {
        new App().start();
        Geometry.getReferenceCache().use(true);
        Mesh.getTextureReferenceCache().use(true);
        // Geometry.getValueCache().use(true);
    }

    App() {
        super("Ingneium Latte", 1600.f, 900.f);
        dLight.setAmbient(new Vec3(0.2, 0.2, 0.2));
        p[0].setIntensity(10);
        p[0].setAmbient(new Vec3());
        p[0].getPosition().setZ(-1.5);
        time.setTargetRenderFPS(75);
    }

    @Override
    protected void onCreate(GL4 gl) {
        setClearColour(gl, 0x404040, 1);
        shader = new Shader(gl, Utils.getFileAsString("./shaders/vert3d.vert"),
                Utils.getFileAsString("./shaders/blinnphong.frag"));

        String textures[][] = new String[][] {
            {"./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg"},
            {"./resource/paper/b.jpg", Utils.NO_VALUE, "./resource/paper/n.jpg"},
            {"./resource/sbrick/b.jpg", "./resource/sbrick/s.jpg", "./resource/sbrick/n.jpg"},
            {"./resource/woodp/b.jpg", Utils.NO_VALUE, "./resource/woodp/n.jpg"},
            {"./resource/mtrim/b.jpg", "./resource/mtrim/s.jpg", "./resource/mtrim/n.jpg"},
            {"./resource/mplate/b.jpg", "./resource/mplate/s.jpg", "./resource/mplate/n.jpg"},
            {"./resource/scrmetal/b.jpg", "./resource/scrmetal/s.jpg", "./resource/scrmetal/n.jpg"},
            {"./resource/gate/b.jpg", "./resource/gate/s.jpg", "./resource/gate/n.jpg"}
        };
        String objectPaths[] = new String[] {
            "./resource/cubent.obj",
            "./resource/suzanne.obj",
            "./resource/uvspherent.obj",
            "./resource/uvsmoothnt.obj",
            "./resource/torusnt.obj",
            "./resource/planent.obj"
        };
        m[0] = new Mesh();
        m[0].make(gl, "./resource/planent.obj", Utils.NO_VALUE, Utils.NO_VALUE, "./resource/woodp/n.jpg");
        m[0].setPosition(new Vec3(m.length >> 1, -7, 0));
        m[0].setScale(new Vec3(m.length >> 1, 1, 10));
        m[0].setTint(new Vec3(0.6, 0.6, 0.6));
        m[0].getMaterial().setShininess(2);
        for (int i = 1; i < m.length; i++) {
            m[i] = new Mesh(new Vec3(i, 0, 0), new Vec3(), new Vec3(1, 1, 1), new Vec3(), new Material(0.2));
            String randomTex[] = textures[(int) (Math.random() * (float) textures.length)];
            m[i].make(gl, objectPaths[(int) (Math.random() * (float) objectPaths.length)], randomTex[0], randomTex[1], randomTex[2]);
            m[i].getMaterial().setShininess(1);
        }

        shader.use(gl);
    }

    double frame = 0;

    @Override
    protected void onRender(GL4 gl) {
        System.out.println("FPS: " + Time.deltaTimeToFPS(time.getRenderDeltaTime()));
        camera.stdControl(input, time.getRenderDeltaTime(), 4, 6);
        frame += 1 * time.getRenderDeltaTime();
        for (int i = 1; i < m.length; i++) {
            double sinVal = Math.sin((float) frame + ((float) i + 1f)) * 5;
            double cosVal = Math.cos((float) frame + ((float) i + 1f)) * 5;
            m[i].getPosition().setY(sinVal);
            m[i].getPosition().setZ(cosVal);
            m[i].setRotation(m[i].getRotation().add(
                    new Vec3(
                        Math.random() + ((float) i / 100), 
                        Math.random() + ((float) i / 100),
                        Math.random() + ((float) i / 100)
                    ).mulFloat(time.getRenderDeltaTime())));
        }
        // double lightVal = Utils.sinPulse(frame, 0.1) * m[m.length - 1].getPosition().getX();
        // p[0].getPosition().setX(lightVal);
        // float sinSpeed = 0.5f;
        // p[0].setDiffuse(new Vec3(Utils.sinPulse(frame, sinSpeed), Utils.sinPulse(frame + 1, sinSpeed), Utils.sinPulse(frame + 2, sinSpeed)));
        // p[0].setSpecular(p[0].getAmbient());
        clear(gl);
        Mesh.renderAll(gl, shader, camera, dLight, m, p);
    }

    @Override
    protected void onClose(GL4 gl) {
        System.out.println("Geometry value cache hits: " + Geometry.getValueCache().getCacheHits());
        System.out.println("Geometry reference cache hits: " + Geometry.getReferenceCache().getCacheHits());
        System.out.println("Texture reference cache hits: " + Mesh.getTextureReferenceCache().getCacheHits());
    }

    @Override
    protected void onUpdate() {

    }
}
