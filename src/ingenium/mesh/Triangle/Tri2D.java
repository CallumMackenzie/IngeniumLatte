package ingenium.mesh.Triangle;

import ingenium.math.Vec2;
import ingenium.math.Vec3;

public class Tri2D extends Tri<Tri2D.Vert> {
    public static class Vert {
        public static final int vertSize = 8;
        public static final int vertByteSize = vertSize * Float.BYTES;

        private Vec2 p; // Point (2)
        private Vec2 t; // Texture coords (2)
        private Vec3 rgb; // RGB tint (4)

        public Vert() {
            return;
        }

        public Vert(Vec2 p, Vec2 t) {
            this.p = p;
            this.t = t;
            this.rgb = new Vec3(1, 1, 1);
        }

        public Vec2 getP() {
            return p;
        }

        public void setP(Vec2 p) {
            this.p = p;
        }

        public Vec2 getT() {
            return t;
        }

        public void setT(Vec2 t) {
            this.t = t;
        }

        public Vec3 getRgb() {
            return rgb;
        }

        public void setRgb(Vec3 rgb) {
            this.rgb = rgb;
        }
    }

    public Tri2D() {
        v = new Tri2D.Vert[3];
    }

    public Tri2D(Vert verts[]) {
        v = verts;
    }

    public void setVert(int index, Tri2D.Vert v) throws IndexOutOfBoundsException {
        this.v[index] = v;
    }

    public Tri2D.Vert getVert(int index) throws IndexOutOfBoundsException {
        return this.v[index];
    }

    public float[] toDataArray() {
        float ret[] = new float[Vert.vertSize * 3];
        for (int i = 0; i < 3; i++) {
            ret[(i * Vert.vertSize) + 0] = (v[i].p.getX());
            ret[(i * Vert.vertSize) + 1] = (v[i].p.getY());

            ret[(i * Vert.vertSize) + 2] = (v[i].t.getX());
            ret[(i * Vert.vertSize) + 3] = (v[i].t.getY());

            ret[(i * Vert.vertSize) + 4] = (v[i].rgb.getX());
            ret[(i * Vert.vertSize) + 5] = (v[i].rgb.getY());
            ret[(i * Vert.vertSize) + 6] = (v[i].rgb.getZ());
            ret[(i * Vert.vertSize) + 7] = (v[i].rgb.getW());
        }
        return ret;
    }
}
