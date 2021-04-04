package ingenium;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Utils {
	public static <T> void printArray (T[] array, String prefix, String suffix) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(prefix.replaceAll("\\{i\\}", Integer.toString(i)));
			System.out.print(array[i]);
			System.out.print(suffix.replaceAll("\\{i\\}", Integer.toString(i)));
		}
	}
	public static String getFileAsString (String path) {
		try {
			String c = new String(Files.readAllBytes(Paths.get(path)));
			return c;
		}
		catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	public static <T> List<T> twoDimensionalArrayToList(T[][] twoDArray) {
		List<T> list = new ArrayList<T>();
		for (T[] array : twoDArray) {
			list.addAll(Arrays.asList(array));
		}
		return list;
	}
	public static void clearConsole () {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	public static int indexOfEnd(String src, String sub) throws StringIndexOutOfBoundsException {
		return src.indexOf(sub) + sub.length();
	}
	public static void sleep (int ms) {
		try
		{
			Thread.sleep(ms);
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}