
package de.geithonline.imageresizer.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class Settings {
    public static SharedPreferences prefs = null;
    private static Context context;

    /**
     * Initializes some preferences on first run with defaults
     * 
     * @param preferences
     */
    public static void initPrefs(final SharedPreferences preferences, final Context context) {
        Settings.context = context;
        Log.i("Settings", "Init Settings-Class");
        prefs = preferences;
        if (prefs.getBoolean("firstrun", true)) {
            Log.i("Settings", "FirstRun --> initializing the SharedPreferences with some colors...");
            prefs.edit().putBoolean("firstrun", false).commit();
            // init colors
            prefs.edit().putBoolean("show_status", false).commit();
        }
    }

    public static boolean isKeepAspectRatio() {
        if (prefs == null) {
            return true;
        }
        return prefs.getBoolean("keepAspectRatio", true);
    }

    public static boolean isShowEasterEggs() {
        if (prefs == null) {
            return true;
        }
        return prefs.getBoolean("easterEggs", true);
    }

    public static boolean isPremium() {
        if (prefs == null) {
            return false;
        }
        return prefs.getBoolean("muimerp", false);
    }

    public static void saveProStatusToPrefs(final boolean isPre) {
        prefs.edit().putBoolean("muimerp", isPre).commit();
    }

    public static boolean isDebugging() {
        if (prefs == null) {
            return false;
        }
        return prefs.getBoolean("debug", false);
    }

    public static int getDisplayWidth(final Context context) {
        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        if (metrics.widthPixels < metrics.heightPixels) {
            return metrics.widthPixels;
        } else {
            return metrics.heightPixels;
        }
    }

    public static void setReadWritePermission(final boolean b) {
        prefs.edit().putBoolean("readWritePermission", b).commit();
    }

    public static boolean isReadWritePermission() {
        if (prefs == null) {
            return false;
        }
        return prefs.getBoolean("readWritePermission", false);
    }

    public static void saveAnyString(final String key, final String value) {
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getAnyString(final String key, final String defaultValue) {
        if (prefs == null) {
            return defaultValue;
        }
        return prefs.getString(key, defaultValue);
    }

}
