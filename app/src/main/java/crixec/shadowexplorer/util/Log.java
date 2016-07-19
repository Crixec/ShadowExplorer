package crixec.shadowexplorer.util;

/**
 * Created by crixec on 16-7-15.
 */
public class Log {
    private static String TAG = "ShadowExplorer";

    public Log() {
    }

    public static void i(CharSequence text) {
        android.util.Log.i(TAG, text.toString());
    }
}
