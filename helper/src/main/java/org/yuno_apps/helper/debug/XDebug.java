package org.yuno_apps.helper.debug;

import de.robv.android.xposed.XposedBridge;

public class XDebug extends Debug {
    public static final boolean ENABLE = true;


    public static void log(String msg) {
        log(null, msg, ENABLE);
    }

    public static void log(Class<?> clazz, String msg) {
        log(clazz, msg, ENABLE);
    }

    public static void log(Class<?> clazz, String msg, boolean force) {
        if (force) {
            XposedBridge.log("> " + msg);
        }
    }

    public static void log(Throwable t) {
        log(null, t, ENABLE);
    }

    public static void log(Class<?> clazz, Throwable t) {
        log(clazz, t, ENABLE);
    }

    public static void log(Class<?> clazz, Throwable t, boolean force) {
        if (force) {
            XposedBridge.log("> " + t);
        }
    }
}
