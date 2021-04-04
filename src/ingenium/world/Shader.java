package ingenium.world;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

public class Shader {
    private int program = -1;

    public Shader(GL2 gl, String vertSource, String fragSource) {
        int vShader = GL2.GL_NONE, fShader = GL2.GL_NONE;
        program = gl.glCreateProgram();
    }

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    public static int compileShader(GL2 gl, int type, String source) {
        int shader = gl.glCreateShader(type);
        String src[] = { source };
        gl.glShaderSource(shader, 1, src, (int[]) null, 0);
        gl.glCompileShader(shader);
        return shader;
    }
}
