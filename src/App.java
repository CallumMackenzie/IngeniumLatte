import ingenium.Ingenium;
import java.util.HashMap;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.utilities.RenderBuffer;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    RenderBuffer postBuffer;
    Mesh2D renderSurface;

    Shader shader3D = new Shader();
    Shader post = new Shader();

    Camera3D camera3D = new Camera3D(9f / 16f, new Vec3(0, 0, -3));
    Camera2D camera2D = new Camera2D(9f / 16f);
    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] {
            new PointLight(new Vec3(-1, 2, -2), new Vec3(), new Vec3(0.2, 1, 1), new Vec3(0.2, 1, 1)) };
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
        // Tell Ingenium we're rendering in 3D
        init3D(gl);
        // Set the clear colour to gray
        setClearColour(gl, 0x404040);
        // Create the 3D shader
        shader3D.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/3D/asn.fs", new HashMap<>() {
            {
                put("maxPointLights", "1");
            }
        });
        // Create the post-processing shader
        post.compileWithParametersFromPath(gl, "./shaders/post/fbo.vs", "./shaders/post/fbo.fs");

        // Create a surface to render to (not necessary if there's no post processing)
        postBuffer = RenderBuffer.createRenderTexture(gl, 1600, 900);
        renderSurface = new Mesh2D();
        renderSurface.make(gl, Ingenium.NO_VALUE);
        renderSurface.getMaterial().setDiffuseTexture(postBuffer.getTextures()[0]);
        renderSurface.setScale(new Vec2(16f / 9f, 1));

        Mesh3D mshs[] = new Mesh3D[12];
        for (int i = 0; i < mshs.length; i++)
            mshs[i] = Mesh3D.createAndMakePreloaded(gl, "./resource/suzanneanim/" + i + ".ing",
                    "./resource/alien/b.jpg", "./resource/alien/s.jpg", "./resource/alien/n.jpg");

        animMesh = new AnimatedMesh<Mesh3D>(gl, Mesh3D.createEmpty(gl, mshs[0].getNumVerts()));
        animMesh.setMeshes(mshs);
        animMesh.setInterpolating(true);
        animMesh.setEndFrame(mshs.length - 1);
        animMesh.setFrameTime(100);
    }

    @Override
    protected void onRender(GL2 gl) {
        // Camera rotation: arrow keys
        // Camera movement: WASD
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);

        // Rotate the mesh, accounting for frame time deviation
        animMesh.getPrimaryMesh().getRotation().addEquals(new Vec3(0, 1, 0).mul(time.getRenderDeltaTime()));

        // Clear the screen
        clear(gl);

        RenderBuffer.renderToRenderTexture(gl, postBuffer);
        animMesh.getPrimaryMesh().render(gl, shader3D, camera3D, dLight, p);
        animMesh.checkAdvanceFrame(gl);
        RenderBuffer.setDefaultRenderBuffer(gl, this);
        renderSurface.render(gl, post, camera2D);
    }

    @Override
    protected void onClose(GL2 gl) {

    }
}
