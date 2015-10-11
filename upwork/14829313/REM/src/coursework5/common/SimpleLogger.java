package coursework5.common;

/**
 * A simple logger tools
 * 
 * @author REM
 *
 */
public class SimpleLogger {

	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int WARN = 2;
	public static final int ERROR = 3;

	private static int level = INFO;

	public static void debug(String msg) {
		if (level <= DEBUG) {
			System.out.println("[DEBUG] : " + msg);
		}
	}

	public static void info(String msg) {
		if (level <= INFO) {
			System.out.println("[INFO] : " + msg);
		}
	}
}
