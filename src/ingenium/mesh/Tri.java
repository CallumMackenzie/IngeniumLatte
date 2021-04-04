package ingenium.mesh;

import ingenium.math.*;

public class Tri {

    public Tri.Vert v[] = new Tri.Vert[3];

    private class Vert {
        public static final int vertSize = 17;

        public Vec3 p = new Vec3(); // Point (4)
        public Vec2 t = new Vec2(); // Texture coords (3)
        public Vec3 rgb = new Vec3(); // RGB tint (4)
        public Vec3 n = new Vec3(); // Normal (3)
        public Vec3 tan = new Vec3(); // Tangent (3)  
    }

    static int getVertByteSize () {
        return Vert.vertSize * Float.BYTES;
    }

    public Vec3[] calculateTangents () {
        Vec3 edge1 = Vec3.sub(this.v[1].p, this.v[0].p);
        Vec3 edge2 = Vec3.sub(this.v[2].p, this.v[0].p);
        Vec2 dUV1 = Vec2.sub(this.v[1].t, this.v[0].t);
        Vec2 dUV2 = Vec2.sub(this.v[2].t, this.v[0].t);

        float f = 1.f / (dUV1.x * dUV2.y - dUV2.x * dUV1.y);

        Vec3 tan = new Vec3();

        tan.x = f * (dUV2.y * edge1.x - dUV1.y * edge2.x);
        tan.y = f * (dUV2.y * edge1.y - dUV1.y * edge2.y);
        tan.z = f * (dUV2.y * edge1.z - dUV1.y * edge2.z);

        Vec3 bitTan = new Vec3();
        bitTan.x = f * (-dUV2.x * edge1.x + dUV1.x * edge2.x);
        bitTan.y = f * (-dUV2.x * edge1.y + dUV1.x * edge2.y);
        bitTan.z = f * (-dUV2.x * edge1.z + dUV1.x * edge2.z);

        Vec3 ret[] = new Vec3[2];
        ret[0] = tan;
        ret[1] = bitTan;

        return ret;
    }
    
    public float[] toDataArray (Mesh m) {
        float ret[] = new float[Vert.vertSize * 3];
        Vec3 tangent[] = calculateTangents();
        for (int i = 0; i < 3; i++) {
            ret[(i * Vert.vertSize) + 0] = (v[i].p.x);
            ret[(i * Vert.vertSize) + 1] = (v[i].p.y);
            ret[(i * Vert.vertSize) + 2] = (v[i].p.z);
            ret[(i * Vert.vertSize) + 3] = (v[i].p.w);

            ret[(i * Vert.vertSize) + 4] = (v[i].t.x);
            ret[(i * Vert.vertSize) + 5] = (v[i].t.y);
            ret[(i * Vert.vertSize) + 6] = (v[i].t.w);

            ret[(i * Vert.vertSize) + 7] = (v[i].rgb.x + m.tint.x);
            ret[(i * Vert.vertSize) + 8] = (v[i].rgb.y + m.tint.y);
            ret[(i * Vert.vertSize) + 9] = (v[i].rgb.z + m.tint.z);
            ret[(i * Vert.vertSize) + 10] = (v[i].rgb.w);

            ret[(i * Vert.vertSize) + 11] = (v[i].n.x);
            ret[(i * Vert.vertSize) + 12] = (v[i].n.y);
            ret[(i * Vert.vertSize) + 13] = (v[i].n.z);

            ret[(i * Vert.vertSize) + 14] = (tangent[0].x);
            ret[(i * Vert.vertSize) + 15] = (tangent[0].y);
            ret[(i * Vert.vertSize) + 16] = (tangent[0].z);
        }
        return ret;
    }
}
