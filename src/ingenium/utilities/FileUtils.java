package ingenium.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {
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
}
