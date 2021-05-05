import ingenium.Ingenium;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Shader shader3D = new Shader();
    Camera3D camera3D = new Camera3D(9f / 16f);
    DirectionalLight dLight = new DirectionalLight();
    Mesh3D mesh;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.start();
    }

    public App() {
        super("Demo", 1600f, 900f); // Create window called "Demo" at resolution 1600 x 900
        time.setTargetRenderFPS(60); // Set the framerate to 60 fps
        showFrame(); // Show the window
    }

    @Override
    protected void onCreate(GL2 gl) {
        init3D(gl); // Tell Ingenium we're rendering in 3D
        setClearColour(gl, 0x404040); // Set the clear colour to gray
        shader3D.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/3D/asn.fs"); // Create the 3D shader
        // Create the mesh with the provided object and texture files
        mesh = Mesh3D.createAndMake(gl, "./resource/sphere.obj", "./resource/metal/b.jpg", "./resource/metal/s.jpg", "./resource/metal/n.jpg");
    }

    @Override
    protected void onRender(GL2 gl) {
        // Camera rotation: arrow keys
        // Camera movement: WASD
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);
        clear(gl); // Clear the screen

        // Rotate the mesh, accounting for frame time deviation
        mesh.setRotation(mesh.getRotation().add(new Vec3(1, 1, 1).mulFloat(time.getRenderDeltaTime()))); 
        mesh.render(gl, shader3D, camera3D, dLight); // Show our 3d object on screen
    }

    @Override
    protected void onClose(GL2 gl) {

    }
}
