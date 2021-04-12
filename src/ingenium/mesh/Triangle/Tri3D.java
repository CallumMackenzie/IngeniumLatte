package ingenium.mesh.Triangle;

import ingenium.math.*;

public class Tri3D extends Tri<Tri3D.Vert> {

    public static class Vert {
        public static final int vertSize = 17;
        public static final int vertByteSize = vertSize * Float.BYTES;

        private Vec3 p; // Point (4)
        private Vec2 t; // Texture coords (3)
        private Vec3 rgb; // RGB tint (4)
        private Vec3 n; // Normal (3)
        private Vec3 tan; // Tangent (3)

        public Vert(Vec3 p, Vec2 t, Vec3 rgb, Vec3 n, Vec3 tan) {
            this.p = p;
            this.t = t;
            this.rgb = rgb;
            this.n = n;
            this.tan = tan;
        }

        public Vert(Vec3 p, Vec2 t, Vec3 rgb, Vec3 n) {
            this(p, t, rgb, n, new Vec3());
        }

        public Vert(Vec3 p, Vec2 t, Vec3 rgb) {
            this(p, t, rgb, new Vec3());
        }

        public Vert(Vec3 p, Vec2 t) {
            this(p, t, new Vec3());
        }

        public Vert(Vec3 p) {
            this(p, new Vec2());
        }

        public Vert() {
            this(new Vec3());
        }

        public void setP(Vec3 p) {
            this.p = p;
        }

        public void setN(Vec3 n) {
            this.n = n;
        }

        public void setT(Vec2 t) {
            this.t = t;
        }

        public void setRgb(Vec3 rgb) {
            this.rgb = rgb;
        }

        public void setTan(Vec3 tan) {
            this.tan = tan;
        }

        public Vec3 getP() {
            return p;
        }

        public Vec3 getN() {
            return n;
        }

        public Vec2 getT() {
            return t;
        }

        public Vec3 getTan() {
            return tan;
        }

        public Vec3 getRgb() {
            return rgb;
        }

        public Tri2D.Vert to2DVert() {
            Tri2D.Vert vert = new Tri2D.Vert();
            vert.setP(p.toVec2());
            vert.setT(t);
            vert.setRgb(rgb);
            return vert;
        }
    }

    public Tri3D() {
        v = new Tri3D.Vert[3];
    }

    public void setVert(int index, Tri3D.Vert v) throws IndexOutOfBoundsException {
        this.v[index] = v;
    }

    public Tri3D.Vert getVert(int index) throws IndexOutOfBoundsException {
        return this.v[index];
    }

    public Vec3[] calculateTangents() {
        Vec3 edge1 = v[1].p.sub(v[0].p);
        Vec3 edge2 = v[2].p.sub(v[0].p);
        Vec2 dUV1 = v[1].t.sub(v[0].t);
        Vec2 dUV2 = v[2].t.sub(v[0].t);

        float div = (dUV1.getX() * dUV2.getY() - dUV2.getX() * dUV1.getY());
        float f = 0.f;
        if (div != 0.f)
            f = 1.f / div;

        Vec3 tan = new Vec3();

        tan.setX(f * (dUV2.getY() * edge1.getX() - dUV1.getY() * edge2.getX()));
        tan.setY(f * (dUV2.getY() * edge1.getY() - dUV1.getY() * edge2.getY()));
        tan.setZ(f * (dUV2.getY() * edge1.getZ() - dUV1.getY() * edge2.getZ()));

        Vec3 bitTan = new Vec3();
        bitTan.setX(f * (-dUV2.getX() * edge1.getX() + dUV1.getX() * edge2.getX()));
        bitTan.setY(f * (-dUV2.getX() * edge1.getY() + dUV1.getX() * edge2.getY()));
        bitTan.setZ(f * (-dUV2.getX() * edge1.getY() + dUV1.getX() * edge2.getZ()));

        Vec3 ret[] = new Vec3[2];
        ret[0] = tan;
        ret[1] = bitTan;

        return ret;
    }

    public Tri2D toTri2D() {
        Tri2D t2d = new Tri2D();
        for (int i = 0; i < 3; i++)
            t2d.setVert(i, v[i].to2DVert());
        return t2d;
    }

    public float[] toDataArray() {
        float ret[] = new float[Vert.vertSize * 3];
        Vec3 tangent[] = calculateTangents();
        for (int i = 0; i < 3; i++) {
            ret[(i * Vert.vertSize) + 0] = (v[i].p.getX());
            ret[(i * Vert.vertSize) + 1] = (v[i].p.getY());
            ret[(i * Vert.vertSize) + 2] = (v[i].p.getZ());
            ret[(i * Vert.vertSize) + 3] = (v[i].p.getW());

            ret[(i * Vert.vertSize) + 4] = (v[i].t.getX());
            ret[(i * Vert.vertSize) + 5] = (v[i].t.getY());
            ret[(i * Vert.vertSize) + 6] = (v[i].t.getW());

            ret[(i * Vert.vertSize) + 7] = (v[i].rgb.getX());
            ret[(i * Vert.vertSize) + 8] = (v[i].rgb.getY());
            ret[(i * Vert.vertSize) + 9] = (v[i].rgb.getZ());
            ret[(i * Vert.vertSize) + 10] = (v[i].rgb.getW());

            ret[(i * Vert.vertSize) + 11] = (v[i].n.getX());
            ret[(i * Vert.vertSize) + 12] = (v[i].n.getY());
            ret[(i * Vert.vertSize) + 13] = (v[i].n.getZ());

            ret[(i * Vert.vertSize) + 14] = (tangent[0].getX());
            ret[(i * Vert.vertSize) + 15] = (tangent[0].getY());
            ret[(i * Vert.vertSize) + 16] = (tangent[0].getZ());
        }
        return ret;
    }
}
