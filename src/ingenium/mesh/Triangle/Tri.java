package ingenium.mesh.Triangle;

public abstract class Tri<vertType> {
    protected vertType v[];

    public vertType[] getV() {
        return v;
    }

    public void setV(vertType[] v) {
        this.v = v;
    }
}
