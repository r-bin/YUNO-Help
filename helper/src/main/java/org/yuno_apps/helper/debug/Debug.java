package org.yuno_apps.helper.debug;

import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

public class Debug {
    public static final boolean ENABLE = BuildConfig.DEBUG;

    // PUBLIC

    public static void log(Class<?> clazz, String msg) {
        log(clazz, msg, ENABLE);
    }

    public static void log(Class<?> clazz, String msg, boolean force) {
        if (force) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void xlog(Class<?> clazz, Exception e) {
        xlog(clazz, e, ENABLE);
    }

    public static void xlog(Class<?> clazz, Exception e, boolean force) {
        if (force) {
            Log.d(clazz.getSimpleName(), e.getMessage());
        }
    }
}
