import ingenium.Ingenium;
import java.util.HashMap;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Shader shader3D = new Shader();

    Camera3D camera3D = new Camera3D(9f / 16f, new Vec3(0, 0, -3));
    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] {};

    Mesh3D meshes[] = new Mesh3D[1];
    AnimatedMesh<Mesh3D> monke;

    public static void main(String[] args) throws Exception {
        Mesh.getTextureReferenceCache().use(true);
        Geometry.getReferenceCache().use(true);

        App app = new App();
        app.start();
    }

    public App() {
        // Create window called "Demo" at resolution 1600 x 900
        super("Demo", 1600, 900);
        // Set the framerate
        time.setTargetRenderFPS(100);
        // Show the window
        showFrame();
    }

    @Override
    protected void onCreate(GL2 gl) {
        // Tell Ingenium we're rendering in 3D
        init3D(gl);
        // Set the clear colour to gray
        setClearColour(gl, 0x404040);

        // Create the 3D shader with at most 1 point light
        shader3D.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/3D/asn.fs", new HashMap<>() {
            {
                put("normalMap", "1");
                put("maxPointLights", "1");
                put("parallaxMap", "0");
                put("parallaxClipEdge", "1");
                put("parallaxInvert", "1");
            }
        });

        meshes[0] = Mesh3D.createAndMake(gl, "./resource/cube.obj", "./resource/metal/b.jpg", "./resource/metal/s.jpg",
                "./resource/metal/n.jpg", "./resource/metal/h.png");
        meshes[0].getMaterial().setShininess(0.1); // Shiny metal
        meshes[0].getMaterial().setParallaxScale(0.1);
        meshes[0].setScale(new Vec3(2, 2, 2));

        Mesh3D[] frames = new Mesh3D[12];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = Mesh3D.createAndMakePreloaded(gl, "./resource/suzanneanim/" + i + ".ing",
                    "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg");
        }
        monke = new AnimatedMesh<Mesh3D>(gl, Mesh3D.createEmpty(gl, frames[0].getNumVerts()), frames, 11, 100);

        for (int i = 0; i < meshes.length; i++)
            meshes[i].setPosition(new Vec3(2.5 * (i + 1)));
    }

    @Override
    protected void onRender(GL2 gl) {
        // Camera rotation: arrow keys
        // Camera movement: WASD
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);

        // Rotate all of our spheres on all 3 axes, accounting for time deviation
        for (Mesh3D mesh3d : meshes)
            mesh3d.getRotation().addEquals(Vec3.filledWith(0.5f).mul(time.getRenderDeltaTime()));
        monke.getPrimaryMesh().getRotation().addEquals(Vec3.filledWith(0.5f).mul(time.getRenderDeltaTime()));

        // Clear the screen
        clear(gl);

        // Mesh3D.renderAll(gl, shader3D, camera3D, dLight, meshes, p);
        monke.checkAdvanceFrame(gl);
        Mesh3D.renderAll(gl, shader3D, camera3D, dLight, new Mesh3D[] { monke.getPrimaryMesh() }, p);
    }

    @Override
    protected void onClose(GL2 gl) {
        for (Mesh3D m : meshes)
            m.delete(gl);
        shader3D.delete(gl);
    }
}
