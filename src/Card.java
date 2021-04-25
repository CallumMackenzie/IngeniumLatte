import com.jogamp.opengl.GL2;
import ingenium.Ingenium;
import ingenium.math.Vec3;
import ingenium.mesh.Mesh3D;
import ingenium.utilities.RenderBuffer;
import ingenium.world.Camera3D;

public class Card extends Mesh3D {
    private static class CardData {
        public String obj;
        public String diffuse;
        public String specular;
        public String parallax;
        public Mesh3D meshes[];

        public CardData(String obj, String diffuse, String specular, String parallax, Mesh3D msh[]) {
            this.obj = obj;
            this.diffuse = diffuse;
            this.specular = specular;
            this.parallax = parallax;
            this.meshes = msh;
        }
    }

    public static final int cardResolution[] = { 500, 500 };
    public static CardData allCards[];

    private RenderBuffer rbo;
    private Mesh3D meshes[];
    // private Mesh2D cardSurface;
    private Camera3D camera = new Camera3D(1.5f, new Vec3(0, 1, 4), new Vec3(0, Math.PI), 90);

    public Card(GL2 gl, String cObj, String cTexturePath, String cSpecMask, String cAlphaMask, Mesh3D meshes[]) {
        super();
        this.rbo = RenderBuffer.createRenderTexture(gl, cardResolution[0], cardResolution[1]);
        // this.rbo[1] = RenderBuffer.createRenderTexture(gl, cardResolution[0],
        // cardResolution[1]);
        this.meshes = meshes;
        this.scale = new Vec3(1, 1, 1.5);
        // this.rotation = new Vec3(-Math.PI / 2f);

        // this.cardSurface = new Mesh2D();
        // this.cardSurface.make(gl, Ingenium.NO_VALUE);
        // this.cardSurface.getMaterial().setDiffuseTexture(this.rbo[0].getTextures()[0]);

        this.make(gl, cObj, cTexturePath, cSpecMask, Ingenium.NO_VALUE, cAlphaMask);
        this.material.setNormalTexture(this.rbo.getTextures()[0]);
    }

    public RenderBuffer getRbo() {
        return rbo;
    }

    public Mesh3D[] getMeshes() {
        return meshes;
    }

    public Camera3D getCamera() {
        return camera;
    }

    public static void init(GL2 gl) {
        allCards = new CardData[] { new CardData("./resource/planent.obj", "./resource/alien/card.png",
                Ingenium.NO_VALUE, "./resource/alien/s.png",
                new Mesh3D[] { Mesh3D.createAndMake(gl, "./resource/alien/Alien.obj", "./resource/alien/b.png") }) };
    }

    public static Card getRandomCard(GL2 gl) {
        CardData cd = allCards[(int) ((float) allCards.length * Math.random())];
        return new Card(gl, cd.obj, cd.diffuse, cd.specular, cd.parallax, cd.meshes);
    }

}