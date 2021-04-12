package ingenium.world;

import java.nio.FloatBuffer;
import java.util.HashMap;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import ingenium.math.Mat2;
import ingenium.math.Mat4;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.utilities.FileUtils;

public class Shader {
    private int program = GL3.GL_NONE;

    /**
     * 
     * @param gl         the GL3 object of the program
     * @param vertSource the source code for the vertex shader
     * @param fragSource the source code for the fragment shader
     */
    public void compile(GL3 gl, String vertSource, String fragSource) {
        int vShader = compileShader(gl, GL3.GL_VERTEX_SHADER, vertSource);
        int fShader = compileShader(gl, GL3.GL_FRAGMENT_SHADER, fragSource);
        program = gl.glCreateProgram();
        gl.glAttachShader(program, vShader);
        gl.glAttachShader(program, fShader);
        gl.glLinkProgram(program);
        gl.glValidateProgram(program);

        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);
    }

    public void compileWithParameters(GL3 gl, String vertSource, String fragSource, HashMap<String, String> gParams,
            HashMap<String, String> vParams, HashMap<String, String> fParams) {
        for (String key : gParams.keySet()) {
            vertSource = vertSource.replace(key, gParams.get(key));
            fragSource = fragSource.replace(key, gParams.get(key));
        }
        for (String key : vParams.keySet())
            vertSource = vertSource.replace(key, vParams.get(key));
        for (String key : fParams.keySet())
            fragSource = fragSource.replace(key, fParams.get(key));
        compile(gl, vertSource, fragSource);
    }

    /**
     * Tells OpenGL to use this shader
     * 
     * @param gl the GL3 object of the program
     */
    public void use(GL3 gl) {
        gl.glUseProgram(program);
    }

    /**
     * 
     * @param gl   the GL3 object of the program
     * @param name the name of the uniform
     * @return the location of the uniform
     */
    public int sLoc(GL3 gl, String name) {
        return gl.glGetUniformLocation(program, name);
    }

    /**
     * 
     * @param <T>      the type to set
     * @param gl       the GL3 object of the program
     * @param name     the name of the uniform in the shader
     * @param value    the value to set
     * @param expanded whether to use vec4 or vec3 for a Vec3, or a vec3 or vec2 for
     *                 a Vec2
     */
    public <T> void setUniform(GL3 gl, String name, T value, boolean expanded) {
        int loc = sLoc(gl, name);
        if (value instanceof Vec3) {
            Vec3 v = (Vec3) value;
            if (expanded)
                gl.glUniform4f(loc, v.getX(), v.getY(), v.getZ(), v.getW());
            else
                gl.glUniform3f(loc, v.getX(), v.getY(), v.getZ());
        } else if (value instanceof Vec2) {
            Vec2 v = (Vec2) value;
            if (expanded)
                gl.glUniform3f(loc, v.getX(), v.getY(), v.getW());
            else
                gl.glUniform2f(loc, v.getX(), v.getY());
        } else if (value instanceof Float) {
            float v = (float) value;
            gl.glUniform1f(loc, v);
        } else if (value instanceof Boolean) {
            boolean v = (boolean) value;
            gl.glUniform1i(loc, v ? 1 : 0);
        } else if (value instanceof Integer) {
            int v = (int) value;
            gl.glUniform1i(loc, v);
        } else if (value instanceof Mat4) {
            Mat4 v = (Mat4) value;
            FloatBuffer f = Buffers.newDirectFloatBuffer(v.flatten());
            gl.glUniformMatrix4fv(loc, 1, false, f);
        } else if (value instanceof Mat2) {
            Mat2 v = (Mat2) value;
            FloatBuffer f = Buffers.newDirectFloatBuffer(v.flatten());
            gl.glUniformMatrix2fv(loc, 1, false, f);
        }
    }

    /**
     * 
     * @param <T>   the type to set
     * @param gl    the GL3 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public <T> void setUniform(GL3 gl, String name, T value) {
        setUniform(gl, name, value, false);
    }

    /**
     * 
     * @param gl    the GL3 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec2(GL3 gl, String name, Vec2 value) {
        gl.glUniform2f(sLoc(gl, name), value.getX(), value.getY());
    }

    /**
     * 
     * @param gl    the GL3 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec3(GL3 gl, String name, Vec2 value) {
        gl.glUniform3f(sLoc(gl, name), value.getX(), value.getY(), value.getW());
    }

    /**
     * 
     * @param gl    the GL3 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec3(GL3 gl, String name, Vec3 value) {
        gl.glUniform3f(sLoc(gl, name), value.getX(), value.getY(), value.getZ());
    }

    /**
     * 
     * @param gl    the GL3 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec4(GL3 gl, String name, Vec3 value) {
        gl.glUniform4f(sLoc(gl, name), value.getX(), value.getY(), value.getZ(), value.getW());
    }

    /**
     * 
     * @param gl    the GL3 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUMat2(GL3 gl, String name, Mat2 value) {
        FloatBuffer f = Buffers.newDirectFloatBuffer(value.flatten());
        gl.glUniformMatrix2fv(sLoc(gl, name), 1, false, f);
    }

    /**
     * 
     * @return the shader program
     */
    public int getProgram() {
        return program;
    }

    /**
     * 
     * @param gl     the GL3 object of the program
     * @param type   the shader type
     * @param source the shader source code
     * @return the location of the compiled shader
     */
    public static int compileShader(GL3 gl, int type, String source) {
        int shader = gl.glCreateShader(type);
        String lines[] = { source };
        int[] lengths = new int[] { lines[0].length() };
        gl.glShaderSource(shader, lines.length, lines, lengths, 0);
        gl.glCompileShader(shader);
        checkCompileStatus(gl, shader);
        return shader;
    }

    private static void checkCompileStatus(GL3 gl, int shader) {
        int[] compiled = new int[1];
        gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != 0)
            System.out.println("Shader compiled");
        else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(shader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(shader, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error compiling shader: " + new String(log));
            System.exit(1);
        }
    }

    public static Shader makeDefault2DShader(GL3 gl, boolean es) {
        Shader shader = new Shader();
        HashMap<String, String> gParams = new HashMap<>();
        HashMap<String, String> vParams = new HashMap<>();
        HashMap<String, String> fParams = new HashMap<>();
        if (es) {
            gParams.put("$version$", "#version 330 es");
            vParams.put("$precision$", "precision highp float;");
            fParams.put("$precision$", "precision mediump float;");
        } else {
            gParams.put("$version$", "#version 330 core");
            gParams.put("$precision$", "");
        }

        shader.compileWithParameters(gl, FileUtils.getFileAsString("./shaders/2D/vert2d.vs"),
                FileUtils.getFileAsString("./shaders/2D/default.fs"), gParams, vParams, fParams);
        return shader;
    }

    public static Shader makeDefault3DShader(GL3 gl, boolean es, int numLights) {
        Shader shader = new Shader();
        HashMap<String, String> gParams = new HashMap<>();
        HashMap<String, String> vParams = new HashMap<>();
        HashMap<String, String> fParams = new HashMap<>();
        if (es) {
            gParams.put("$version$", "#version 330 es");
            vParams.put("$precision$", "precision highp float;");
            fParams.put("$precision$", "precision mediump float;");
        } else {
            gParams.put("$version$", "#version 330 core");
            gParams.put("$precision$", "");
        }
        fParams.put("$nlights$", Integer.toString(numLights));

        shader.compileWithParameters(gl, FileUtils.getFileAsString("./shaders/3D/vert3d.vs"),
                FileUtils.getFileAsString("./shaders/3D/blinnphong.fs"), gParams, vParams, fParams);
        return shader;
    }
}
