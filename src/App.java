import ingenium.Ingenium;

import java.util.HashMap;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.utilities.RenderBuffer;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Shader shader2D = new Shader();
    Shader shader3D = new Shader();
    Shader post = new Shader();

    Mesh2D m2D[] = new Mesh2D[1];
    Mesh3D m3D[] = new Mesh3D[100];

    Camera2D camera2d = new Camera2D(9f / 16f);
    Camera3D camera3D = new Camera3D(9.f / 16.f);

    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] { new PointLight(new Vec3(0, 1, 2.5)) };

    RenderBuffer rBuffer;

    public static void main(String[] args) throws Exception {
        Geometry.getReferenceCache().use(true);
        Geometry.getValueCache().use(true);
        Mesh3D.getTextureReferenceCache().use(true);
        new App().start();
    }

    public App() {
        super("Ingenium Demo App", 720.f, 480.f);
        time.setTargetRenderFPS(75);
        dLight.setIntensity(0.5f);
        p[0].setIntensity(10);
        p[0].setAmbient(new Vec3());
        p[0].getPosition().setZ(-1.5);
        time.setTargetRenderFPS(80);
    }

    @Override
    protected void onCreate(GL2 gl) {
        init2D(gl);
        init3D(gl);
        setClearColour(gl, 0x404040, 1);
        shader3D.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/3D/asn.fs", new HashMap<>() {{
            put("maxPointLights", "1");
        }});
        shader2D.compileWithParametersFromPath(gl, "./shaders/2D/vert2d.vs", "./shaders/2D/default.fs");
        post.compileWithParametersFromPath(gl, "./shaders/post/fbo.vs", "./shaders/post/fbo.fs");
        rBuffer = RenderBuffer.createRenderTexture(gl, 720, 480);

        String objectPaths[] = new String[] { "./resource/cubent.obj", "./resource/suzanne.obj",
                "./resource/uvspherent.obj", "./resource/uvsmoothnt.obj", "./resource/torusnt.obj",
                "./resource/planent.obj" };

        String textures[][] = new String[][] {
                { "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg" },
                { "./resource/paper/b.jpg", Ingenium.NO_VALUE, "./resource/paper/n.jpg" },
                { "./resource/sbrick/b.jpg", "./resource/sbrick/s.jpg", "./resource/sbrick/n.jpg" },
                { "./resource/woodp/b.jpg", Ingenium.NO_VALUE, "./resource/woodp/n.jpg" },
                { "./resource/mtrim/b.jpg", "./resource/mtrim/s.jpg", "./resource/mtrim/n.jpg" },
                { "./resource/mplate/b.jpg", "./resource/mplate/s.jpg", "./resource/mplate/n.jpg" },
                { "./resource/scrmetal/b.jpg", "./resource/scrmetal/s.jpg", "./resource/scrmetal/n.jpg" },
                { "./resource/gate/b.jpg", "./resource/gate/s.jpg", "./resource/gate/n.jpg" } };

        m3D[0] = new Mesh3D();
        m3D[0].make(gl, "./resource/planent.obj", "./resource/paper/r.jpg", Ingenium.NO_VALUE,
                "./resource/paper/n.jpg");
        m3D[0].setPosition(new Vec3(m3D.length >> 1, -7, 0));
        m3D[0].setScale(new Vec3(m3D.length >> 1, 1, 10));
        m3D[0].setTint(new Vec3(0.6, 0.6, 0.6));
        m3D[0].getMaterial().setShininess(2);
        for (int i = 1; i < m3D.length; i++) {
            m3D[i] = new Mesh3D(new Vec3(i, 0, 0), new Vec3(), new Vec3(1, 1, 1), new Vec3(), new Material(0.2));
            String randomTex[] = textures[(int) (Math.random() * (float) textures.length)];
            m3D[i].make(gl, objectPaths[(int) (Math.random() * (float) objectPaths.length)], randomTex[0], randomTex[1],
                    randomTex[2]);
            m3D[i].getMaterial().setShininess(1.3);
        }

        m2D[0] = new Mesh2D();
        // m2D[0].setScale(new Vec2(1.5, 0.1));
        // m2D[0].setPosition(new Vec2(0, -0.75));
        m2D[0].make(gl, Ingenium.NO_VALUE);
        m2D[0].getMaterial().setDiffuseTexture(rBuffer.getTextures()[0]);
    }

    double frame = 0;

    @Override
    protected void onRender(GL2 gl) {
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);
        frame += 1 * time.getRenderDeltaTime();
        for (int i = 1; i < m3D.length; i++) {
            double sinVal = Math.sin((float) frame + ((float) i + 1f)) * 5;
            double cosVal = Math.cos((float) frame + ((float) i + 1f)) * 5;
            m3D[i].getPosition().setY(sinVal);
            m3D[i].getPosition().setZ(cosVal);
            m3D[i].setRotation(m3D[i].getRotation()
                    .add(new Vec3(Math.random() + ((float) i / 100), Math.random() + ((float) i / 100),
                            Math.random() + ((float) i / 100)).mulFloat(time.getRenderDeltaTime())));
        }
        double lightVal = Functions.sinPulse(frame, 0.1) * m3D[m3D.length - 1].getPosition().getX();
        p[0].getPosition().setX(lightVal);
        float sinSpeed = 0.5f;
        p[0].setDiffuse(new Vec3(Functions.sinPulse(frame, sinSpeed), Functions.sinPulse(frame + 1, sinSpeed),
                Functions.sinPulse(frame + 2, sinSpeed)));
        p[0].setSpecular(p[0].getAmbient());

        clear(gl);
        RenderBuffer.renderToRenderTexture(gl, rBuffer);
        Mesh3D.renderAll(gl, shader3D, camera3D, dLight, m3D, p);
        RenderBuffer.setDefaultRenderBuffer(gl, this);
        Mesh2D.renderAll(gl, post, camera2d, m2D);
    }

    @Override
    protected void onClose(GL2 gl) {
        System.out.println("Geometry value cache hits: " + Geometry.getValueCache().getCacheHits());
        System.out.println("Geometry reference cache hits: " + Geometry.getReferenceCache().getCacheHits());
        System.out.println("Texture reference cache hits: " + Mesh3D.getTextureReferenceCache().getCacheHits());
    }
}
