import ingenium.Ingenium;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Shader shader3D = new Shader();
    Camera3D camera3D = new Camera3D(9f / 16f, new Vec3(0, 0, -3));
    DirectionalLight dLight = new DirectionalLight();
    AnimatedMesh<Mesh3D> animMesh;

    public static void main(String[] args) throws Exception {
        Mesh.getTextureReferenceCache().use(true);
        Geometry.getReferenceCache().use(true);
        App app = new App();
        app.start();
    }

    public App() {
        super("Demo", 1600f, 900f); // Create window called "Demo" at resolution 1600 x 900
        time.setTargetRenderFPS(100); // Set the framerate to 60 fps
        showFrame(); // Show the window
    }

    @Override
    protected void onCreate(GL2 gl) {
        init3D(gl); // Tell Ingenium we're rendering in 3D
        setClearColour(gl, 0x404040); // Set the clear colour to gray
        shader3D.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/3D/asn.fs"); // Create the 3D shader

        Mesh3D mshs[] = new Mesh3D[12];
        for (int i = 0; i < mshs.length; i++) {
            mshs[i] = Mesh3D.createAndMakePreloaded(gl, "./resource/sphereanim/" + i + ".ing", "./resource/alien/b.jpg", "./resource/alien/s.jpg", "./resource/alien/n.jpg");
            // Geometry.createPreloadedModel("./resource/sphereanim/s_" + "0".repeat(6 - Integer.toString(i).length()) + i + ".obj", "./resource/sphereanim/" + i + ".ing");
            mshs[i].getMaterial().setShininess(0.4);
        }
        // System.exit(0);
        animMesh = new AnimatedMesh<Mesh3D>(gl, Mesh3D.createEmpty(gl, mshs[0].getNumVerts()));
        animMesh.setInterpolating(true);
        animMesh.setMeshes(mshs);
        animMesh.setEndFrame(mshs.length - 1);
        animMesh.setFrameTime(100);
    }

    @Override
    protected void onRender(GL2 gl) {
        // Camera rotation: arrow keys
        // Camera movement: WASD
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);
        clear(gl); // Clear the screen

        // Rotate the mesh, accounting for frame time deviation
        animMesh.getPrimaryMesh().setRotation(animMesh.getPrimaryMesh().getRotation().add(new Vec3(1, 1, 1).mulFloat(time.getRenderDeltaTime()))); 

        animMesh.getPrimaryMesh().render(gl, shader3D, camera3D, dLight);
        animMesh.checkAdvanceFrame(gl);
    }

    @Override
    protected void onClose(GL2 gl) {

    }
}
