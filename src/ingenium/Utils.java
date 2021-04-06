package ingenium;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL4;

import java.util.Arrays;

public class Utils {
    public static <T> void printArray(T[] array, String prefix, String suffix) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(prefix.replaceAll("\\{i\\}", Integer.toString(i)));
            System.out.print(array[i]);
            System.out.print(suffix.replaceAll("\\{i\\}", Integer.toString(i)));
        }
    }

    /**
     * 
     * @param path the path to the file
     * @return the contents of the file, or null if the file could not be found/read
     */
    public static String getFileAsString(String path) {
        try {
            String c = new String(Files.readAllBytes(Paths.get(path)));
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param <T>       the array type
     * @param twoDArray the two dimensional array
     * @return a flattened List
     */
    public static <T> List<T> twoDimensionalArrayToList(T[][] twoDArray) {
        List<T> list = new ArrayList<T>();
        for (T[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    /**
     * Clears the console
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * 
     * @param src source string
     * @param sub substring to find
     * @return the index of the end of the substring
     * @throws StringIndexOutOfBoundsException
     */
    public static int indexOfEnd(String src, String sub) throws StringIndexOutOfBoundsException {
        return src.indexOf(sub) + sub.length();
    }

    /**
     * Sleeps the current thread
     * 
     * @param ms time to sleep in milleseconds
     */
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks for any OpenGL errors
     * 
     * @param gl the GL4 object of the program
     */
    public static void peekGLErrors(GL4 gl) {
        int err;
        while ((err = gl.glGetError()) != GL4.GL_NO_ERROR) {
            System.err.println("OpenGL Error Code: " + Integer.toHexString(err));
        }
    }
}