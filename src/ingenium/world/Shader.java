package ingenium.world;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.FloatBuffer;
import java.util.HashMap;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import ingenium.math.Mat2;
import ingenium.math.Mat4;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.utilities.FileUtils;

public class Shader {
    private static final Pattern SHADER_VAR_REGEX = Pattern.compile("\\$.+\\(.*\\)\\$");

    public static class Uniforms {
        public static final String material_diffuse = "material.diffuse";
        public static final String material_specular = "material.specular";
        public static final String material_normal = "material.normal";
        public static final String material_parallax = "material.parallax";
        public static final String material_option = "material.option";
        public static final String material_heightScale = "material.heightScale";
        public static final String material_shininess = "material.shininess";
        public static final String material_scaleUVLoc = "mesh.scaleUV";
        public static final String pointLight_structName = "pointLights";
        public static final String pointLight_position = "position";
        public static final String pointLight_ambient = "ambient";
        public static final String pointLight_diffuse = "diffuse";
        public static final String pointLight_specular = "specular";
        public static final String pointLight_constant = "constant";
        public static final String pointLight_linear = "linear";
        public static final String pointLight_quadratic = "quadratic";
        public static final String directionalLight_direction = "dirLight.direction";
        public static final String directionalLight_ambient = "dirLight.ambient";
        public static final String directionalLight_specular = "dirLight.specular";
        public static final String directionalLight_diffuse = "dirLight.diffuse";
        public static final String mesh3D_modelMatrix = "mesh.transform";
        public static final String mesh3D_invModelMatrix = "mesh.inverseTransform";
        public static final String mesh3D_tint = "mesh.tint";
        public static final String ingenium_time = "u_time";
        public static final String camera3D_view = "camera.view";
        public static final String camera3D_projection = "camera.projection";
        public static final String camera3D_viewPos = "viewPos";
        public static final String shader_numLights = "numlights";
        public static final String camera2D_translation = "camera.translation";
        public static final String camera2D_rotation = "camera.rotation";
        public static final String camera2D_rotationPoint = "camera.rotationPoint";
        public static final String camera2D_aspect = "camera.aspect";
        public static final String mesh2D_tint = "model.tint";
        public static final String mesh2D_translation = "model.translation";
        public static final String mesh2D_rotation = "model.rotation";
        public static final String mesh2D_rotationPoint = "model.rotationPoint";
        public static final String mesh2D_scale = "model.scale";
        public static final String mesh2D_zIndex = "model.zIndex";
    }

    private int program = GL2.GL_NONE;
    private static String defaultVersion = "330";
    private static String defaultESVersion = "320";

    /**
     * 
     * @param gl         the GL2 object of the program
     * @param vertSource the source code for the vertex shader
     * @param fragSource the source code for the fragment shader
     */
    public void compile(GL2 gl, String vertSource, String fragSource) {
        if (program != GL2.GL_NONE) 
            delete(gl);
        int vShader = compileShader(gl, GL2.GL_VERTEX_SHADER, vertSource);
        int fShader = compileShader(gl, GL2.GL_FRAGMENT_SHADER, fragSource);
        program = gl.glCreateProgram();
        gl.glAttachShader(program, vShader);
        gl.glAttachShader(program, fShader);
        gl.glLinkProgram(program);
        gl.glValidateProgram(program);

        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);
    }

    public void delete(GL2 gl) {
        if (program == GL2.GL_NONE)
            return;
        gl.glDeleteProgram(program);
        program = GL2.GL_NONE;
    }

    public void compileWithParameters(GL2 gl, String vertSource, String fragSource, HashMap<String, String> gParams,
            HashMap<String, String> vParams, HashMap<String, String> fParams) {
        Matcher varMatcher = SHADER_VAR_REGEX.matcher(vertSource);
        while (varMatcher.find()) {
            String rawVar = varMatcher.group().replace("$", "");
            String varName = rawVar.substring(0, rawVar.indexOf("(")) + rawVar.substring(rawVar.lastIndexOf(")") + 1);
            String defValue = rawVar.substring(rawVar.indexOf("(") + 1, rawVar.lastIndexOf(")"));
            if (vParams.containsKey(varName)) {
                vertSource = vertSource.replace(rawVar, vParams.get(varName));
            } else if (gParams.containsKey(varName)) {
                vertSource = vertSource.replace(rawVar, gParams.get(varName));
            } else {
                vertSource = vertSource.replace(rawVar, defValue);
            }
        }
        varMatcher = SHADER_VAR_REGEX.matcher(fragSource);
        while (varMatcher.find()) {
            String rawVar = varMatcher.group().replace("$", "");
            String varName = rawVar.substring(0, rawVar.indexOf("(")) + rawVar.substring(rawVar.lastIndexOf(")") + 1);
            String defValue = rawVar.substring(rawVar.indexOf("(") + 1, rawVar.lastIndexOf(")"));
            if (fParams.containsKey(varName)) {
                fragSource = fragSource.replace(rawVar, fParams.get(varName));
            } else if (gParams.containsKey(varName)) {
                fragSource = fragSource.replace(rawVar, gParams.get(varName));
            } else {
                fragSource = fragSource.replace(rawVar, defValue);
            }
        }
        vertSource = vertSource.replace("$", "");
        fragSource = fragSource.replace("$", "");
        compile(gl, vertSource, fragSource);
    }

    public void compileWithParametersFromPath(GL2 gl, String vertPath, String fragPath, HashMap<String, String> gParams,
            HashMap<String, String> vParams, HashMap<String, String> fParams) {
        compileWithParameters(gl, FileUtils.getFileAsString(vertPath), FileUtils.getFileAsString(fragPath), gParams,
                vParams, fParams);
    }

    public void compileWithParametersFromPath(GL2 gl, String vertPath, String fragPath, HashMap<String, String> gParams,
            HashMap<String, String> vParams) {
        compileWithParametersFromPath(gl, vertPath, fragPath, gParams, vParams, new HashMap<>());
    }

    public void compileWithParametersFromPath(GL2 gl, String vertPath, String fragPath,
            HashMap<String, String> gParams) {
        compileWithParametersFromPath(gl, vertPath, fragPath, gParams, new HashMap<>());
    }

    public void compileWithParametersFromPath(GL2 gl, String vertPath, String fragPath) {
        compileWithParametersFromPath(gl, vertPath, fragPath, new HashMap<>(), new HashMap<>());
    }

    /**
     * Tells OpenGL to use this shader
     * 
     * @param gl the GL2 object of the program
     */
    public void use(GL2 gl) {
        gl.glUseProgram(program);
    }

    /**
     * 
     * @param gl   the GL2 object of the program
     * @param name the name of the uniform
     * @return the location of the uniform
     */
    public int sLoc(GL2 gl, String name) {
        return gl.glGetUniformLocation(program, name);
    }

    /**
     * 
     * @param <T>      the type to set
     * @param gl       the GL2 object of the program
     * @param name     the name of the uniform in the shader
     * @param value    the value to set
     * @param expanded whether to use vec4 or vec3 for a Vec3, or a vec3 or vec2 for
     *                 a Vec2
     */
    public <T> void setUniform(GL2 gl, String name, T value, boolean expanded) {
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
     * @param gl    the GL2 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public <T> void setUniform(GL2 gl, String name, T value) {
        setUniform(gl, name, value, false);
    }

    /**
     * 
     * @param gl    the GL2 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec2(GL2 gl, String name, Vec2 value) {
        gl.glUniform2f(sLoc(gl, name), value.getX(), value.getY());
    }

    /**
     * 
     * @param gl    the GL2 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec3(GL2 gl, String name, Vec2 value) {
        gl.glUniform3f(sLoc(gl, name), value.getX(), value.getY(), value.getW());
    }

    /**
     * 
     * @param gl    the GL2 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec3(GL2 gl, String name, Vec3 value) {
        gl.glUniform3f(sLoc(gl, name), value.getX(), value.getY(), value.getZ());
    }

    /**
     * 
     * @param gl    the GL2 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUVec4(GL2 gl, String name, Vec3 value) {
        gl.glUniform4f(sLoc(gl, name), value.getX(), value.getY(), value.getZ(), value.getW());
    }

    /**
     * 
     * @param gl    the GL2 object of the program
     * @param name  the name of the uniform in the shader
     * @param value the value to set
     */
    public void setUMat2(GL2 gl, String name, Mat2 value) {
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
     * @param gl     the GL2 object of the program
     * @param type   the shader type
     * @param source the shader source code
     * @return the location of the compiled shader
     */
    public static int compileShader(GL2 gl, int type, String source) {
        int shader = gl.glCreateShader(type);
        String lines[] = { source };
        int[] lengths = new int[] { lines[0].length() };
        gl.glShaderSource(shader, lines.length, lines, lengths, 0);
        gl.glCompileShader(shader);
        checkCompileStatus(gl, shader);
        return shader;
    }

    private static void checkCompileStatus(GL2 gl, int shader) {
        int[] compiled = new int[1];
        gl.glGetShaderiv(shader, GL2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != 0)
            System.out.println("Shader compiled");
        else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(shader, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error compiling shader: " + new String(log));
            System.exit(1);
        }
    }

    public static String getDefaultESVersion() {
        return defaultESVersion;
    }

    public static void setDefaultESVersion(String defaultESVersion) {
        Shader.defaultESVersion = defaultESVersion;
    }

    public static String getDefaultVersion() {
        return defaultVersion;
    }

    public static void setDefaultVersion(String defaultVersion) {
        Shader.defaultVersion = defaultVersion;
    }
}
