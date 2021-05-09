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
            new PointLight(new Vec3(-1, 2, -2), new Vec3(), new Vec3(0.5, 1, 1), new Vec3(0.5, 1, 1)) };
    AnimatedMesh<Mesh3D> animMesh;

    Mesh3D meshes[] = new Mesh3D[2];

    public static void main(String[] args) throws Exception {
        // Caching textures makes loading way faster
        Mesh.getTextureReferenceCache().use(true);
        // Caching geometry also makes loading way faster
        Geometry.getReferenceCache().use(true);

        // Start the app
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
                put("maxPointLights", "1");
            }
        });
        // Create the post-processing shader
        post.compileWithParametersFromPath(gl, "./shaders/post/fbo.vs", "./shaders/post/fbo.fs");

        // Create a surface to render to
        postBuffer = RenderBuffer.createRenderTexture(gl, 1600, 900);
        // Create a quad to draw to as a buffer before the screen
        renderSurface = postBuffer.createRenderQuad(gl);

        // Add the frames to the animated mesh
        Mesh3D mshs[] = new Mesh3D[12];
        for (int i = 0; i < mshs.length; i++) {
            // Create the mesh from a pre-processed object
            mshs[i] = Mesh3D.createAndMakePreloaded(gl, "./resource/suzanneanim/" + i + ".ing",
                    "./resource/alien/b.jpg", "./resource/alien/s.jpg", "./resource/alien/n.jpg");
        }

        // Create an animated mesh with our frames and 175 ms between the frames
        // The animated mesh will interpolate between frames by default
        animMesh = new AnimatedMesh<>(gl, Mesh3D.createEmpty(gl, mshs[0].getNumVerts()), mshs, mshs.length - 1, 175);

        // Create some textured spheres
        meshes[0] = Mesh3D.createAndMake(gl, "./resource/sphere.obj", "./resource/metal/b.jpg",
                "./resource/metal/s.jpg", "./resource/metal/n.jpg");
        meshes[0].getMaterial().setShininess(0.1); // Shiny metal

        meshes[1] = Mesh3D.createAndMake(gl, "./resource/sphere.obj", "./resource/alien/b.jpg",
                "./resource/alien/s.jpg", "./resource/alien/n.jpg");

        for (int i = 0; i < meshes.length; i++)
            meshes[i].setPosition(new Vec3(2.5 * (i + 1)));
    }

    @Override
    protected void onRender(GL2 gl) {
        // Camera rotation: arrow keys
        // Camera movement: WASD
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);
        // Make the point light follow the player
        p[0].setPosition(camera3D.getPosition());

        // Rotate the mesh, accounting for frame time deviation
        animMesh.getPrimaryMesh().getRotation().addEquals(new Vec3(0, 1, 0).mul(time.getRenderDeltaTime()));
        // Advance animation
        animMesh.checkAdvanceFrame(gl);

        // Rotate all of our spheres on all 3 axes, accounting for time deviation
        for (Mesh3D mesh3d : meshes)
            mesh3d.getRotation().addEquals(new Vec3(1, 1, 1).mul(time.getRenderDeltaTime()));

        // Clear the screen
        clear(gl);

        // Render to the postprocessing texture
        RenderBuffer.renderToRenderTexture(gl, postBuffer);
        Mesh3D.renderAll(gl, shader3D, camera3D, dLight, meshes, p);
        animMesh.getPrimaryMesh().render(gl, shader3D, camera3D, dLight, p);

        // Render our post texture to the screen
        RenderBuffer.setDefaultRenderBuffer(gl, this);
        renderSurface.render(gl, post, camera2D);
    }
}
