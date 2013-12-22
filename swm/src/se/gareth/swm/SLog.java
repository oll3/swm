package se.gareth.swm;
import android.util.Log;


public class SLog {

	public static boolean enable = true;

	/*
	 * Verbose message
	 */
	public static void v(final String tag, final String msg) {
		if (enable)
			Log.v(tag, msg);
	}

	/*
	 * Debug message
	 */	
	public static void d(final String tag, final String msg) {
		if (enable)
			Log.d(tag, msg);
	}
	

	/*
	 * Info message
	 */	
	public static void i(final String tag, final String msg) {
		if (enable)
			Log.i(tag, msg);
	}

	
	/*
	 * Warning message
	 */	
	public static void w(final String tag, final String msg) {
		if (enable)
			Log.w(tag, msg);
	}


	/*
	 * Error message
	 */	
	public static void e(final String tag, final String msg) {
		if (enable)
			Log.e(tag, msg);
	}
	
	public static void pe(final String tag, final String msg, java.lang.Exception e) {
		if (enable)
			Log.e(tag, e.getMessage(), e);
	}

}
