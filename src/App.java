import ingenium.Ingenium;

import java.util.HashMap;
import com.jogamp.opengl.GL2;
import ingenium.math.*;
import ingenium.mesh.*;
import ingenium.utilities.RenderBuffer;
import ingenium.world.*;
import ingenium.world.light.*;

public class App extends Ingenium {
    Player player = new Player(3);
    RenderBuffer postBuffer;
    Mesh2D renderSurface;

    Shader shader2D = new Shader();
    Shader shader3D = new Shader();
    Shader post = new Shader();
    Shader cardShader = new Shader();

    Camera2D camera2d = new Camera2D(9f / 16f);
    Camera3D camera3D = new Camera3D(9f / 16f);

    DirectionalLight dLight = new DirectionalLight();
    PointLight p[] = new PointLight[] {
            new PointLight(new Vec3(0, 2, 0), new Vec3(0.05, 0.1, 0.08), new Vec3(0.1, 0.9, 0.2), new Vec3(0.2, 0.7, 0.4))
    };

    public static void main(String[] args) throws Exception {
        Geometry.getReferenceCache().use(true);
        Geometry.getValueCache().use(true);
        Mesh3D.getTextureReferenceCache().use(true);
        new App().start();
    }

    public App() {
        super("Card Game", 1600f, 900f);
        time.setTargetRenderFPS(80);
        // frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);
        showFrame();
    }

    @Override
    protected void onCreate(GL2 gl) {
        init2D(gl);
        init3D(gl);
        gl.glDisable(GL2.GL_CULL_FACE);
        setClearColour(gl, 0x404040, 1);
        Card.init(gl);
        shader3D.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/3D/asn.fs", new HashMap<>() {
            {
                put("maxPointLights", "1");
                put("normalMap", "0");
            }
        });
        cardShader.compileWithParametersFromPath(gl, "./shaders/3D/asn.vs", "./shaders/card.fs", new HashMap<>() {
            {
                put("normalMap", "1");
                put("parallaxMap", "0");
                put("parallaxClipEdge", "1");
                put("maxPointLights", "0");
                put("lightModel", "BLINN");
            }
        });
        shader2D.compileWithParametersFromPath(gl, "./shaders/2D/vert2d.vs", "./shaders/2D/default.fs");
        post.compileWithParametersFromPath(gl, "./shaders/post/fbo.vs", "./shaders/post/fbo.fs");

        postBuffer = RenderBuffer.createRenderTexture(gl, 1600, 900);

        renderSurface = new Mesh2D();
        renderSurface.make(gl, Ingenium.NO_VALUE);
        renderSurface.getMaterial().setDiffuseTexture(postBuffer.getTextures()[0]);
        renderSurface.setScale(new Vec2(16f / 9f, 1));

        player.setRandomCards(gl);
        dLight.setDirection(new Vec3(-0.4, -1, -0.4));
        dLight.setIntensity(0.9f);
    }

    @Override
    protected void onRender(GL2 gl) {
        camera3D.stdControl(input, time.getRenderDeltaTime(), 4, 6);
        player.update(time);

        clear(gl);
        for (Card c : player.getCards()) {
            RenderBuffer.renderToRenderTexture(gl, c.getRbo());
            Mesh3D.renderAll(gl, shader3D, c.getCamera(), dLight, c.getMeshes(), p);
        }
        RenderBuffer.renderToRenderTexture(gl, postBuffer);
        Mesh3D.renderAll(gl, cardShader, camera3D, dLight, player.getCards(), p);
        RenderBuffer.setDefaultRenderBuffer(gl, this);
        renderSurface.render(gl, post, camera2d);
    }

    @Override
    protected void onClose(GL2 gl) {

    }
}
