import com.jogamp.opengl.GL2;
import ingenium.Ingenium;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.mesh.Geometry;
import ingenium.mesh.Mesh3D;
import ingenium.utilities.RenderBuffer;
import ingenium.world.Camera3D;

public class Card extends Mesh3D {
    private static class CardData {
        public String obj;
        public String diffuse;
        public String specular;
        public String normal;
        public String parallax;
        public String alphaMask;
        public Mesh3D meshes[];

        public CardData(String obj, String diffuse, String specular, String normal, String parallax, String alphaMask,
                Mesh3D msh[]) {
            this.obj = obj;
            this.diffuse = diffuse;
            this.normal = normal;
            this.specular = specular;
            this.parallax = parallax;
            this.meshes = msh;
            this.alphaMask = alphaMask;
        }
    }

    public static final int cardResolution[] = { 700, 700 };
    public static CardData allCards[];

    private RenderBuffer rbo;
    private Mesh3D meshes[];
    private Camera3D camera = new Camera3D(1.25f / 1.5f, new Vec3(2.2, 1, 4), new Vec3(0, Math.PI), 90);

    public Card(GL2 gl, String cObj, String cDiffusePath, String cSpecPath, String cNormalPath, String cBumpPath,
            String cAlphaMask, Mesh3D meshes[]) {
        super();
        this.rbo = RenderBuffer.createRenderTexture(gl, cardResolution[0], cardResolution[1]);
        this.meshes = meshes;
        // this.rotation = new Vec3(Math.PI);
        this.scale = new Vec3(0.7, 0.7, 0.7);
        this.material.setParallaxScale(0.04);
        this.material.setShininess(1);

        this.make(gl, cObj, cDiffusePath, cSpecPath, cNormalPath, cBumpPath);
        this.material.setOptionTextures(
                new int[] { this.rbo.getTextures()[0], findAndLoadTexture(gl, cAlphaMask, GL2.GL_TEXTURE5, true) });
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
        allCards = new CardData[] { new CardData("./resource/card.obj", "./resource/alien/cb.png",
                "./resource/alien/cs.png", "./resource/alien/cn.png", "./resource/alien/cs.png", "./resource/alien/ch.png",
                new Mesh3D[] {
                        Mesh3D.createAndMakePreloaded(gl, "./resource/alien/alien.ilo", "./resource/alien/b.png") }) };
    }

    public static Card getRandomCard(GL2 gl) {
        CardData cd = allCards[(int) ((float) allCards.length * Math.random())];
        return new Card(gl, cd.obj, cd.diffuse, cd.specular, cd.normal, cd.parallax, cd.alphaMask, cd.meshes);
    }

}