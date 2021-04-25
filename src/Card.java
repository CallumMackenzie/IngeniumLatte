import com.jogamp.opengl.GL2;
import ingenium.Ingenium;
import ingenium.math.Vec3;
import ingenium.mesh.Mesh3D;
import ingenium.utilities.RenderBuffer;
import ingenium.world.Camera3D;

public class Card extends Mesh3D {
    public static final int cardResolution[] = { 500, 500 };
    public static Card allCards[];

    private RenderBuffer rbo[] = new RenderBuffer[2];
    private Mesh3D meshes[];
    // private Mesh2D cardSurface;
    private Camera3D camera = new Camera3D(1.5f, new Vec3(0, 1, 4), new Vec3(0, Math.PI), 90);

    public Card(GL2 gl, String cardTexturePath, String cardAlphaMask, Mesh3D meshes[]) {
        super();
        this.rbo[0] = RenderBuffer.createRenderTexture(gl, cardResolution[0], cardResolution[1]);
        // this.rbo[1] = RenderBuffer.createRenderTexture(gl, cardResolution[0], cardResolution[1]);
        this.meshes = meshes;
        this.scale = new Vec3(1, 1, 1.5);
        this.rotation = new Vec3(-Math.PI / 2f);
        // this.cardSurface = new Mesh2D();
        // this.cardSurface.make(gl, Ingenium.NO_VALUE);
        // this.cardSurface.getMaterial().setDiffuseTexture(this.rbo[0].getTextures()[0]);

        this.make(gl, "./resource/planent.obj", cardTexturePath, cardAlphaMask, Ingenium.NO_VALUE);
        this.material.setNormalTexture(this.rbo[0].getTextures()[0]);
        this.material.setShininess(0.1);
    }

    public RenderBuffer[] getRbo() {
        return rbo;
    }
    public Mesh3D[] getMeshes() {
        return meshes;
    }
    public Camera3D getCamera() {
        return camera;
    }

    public static void init(GL2 gl) {
        allCards = new Card[] { new Card(gl, "./resource/alien/card.png", "",
                new Mesh3D[] { Mesh3D.createAndMake(gl, "./resource/alien/Alien.obj", "./resource/alien/b.png") }) };
    }

}