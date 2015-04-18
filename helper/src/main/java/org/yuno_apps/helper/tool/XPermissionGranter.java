package org.yuno_apps.helper.tool;

import org.yuno_apps.helper.debug.XDebug;
import org.yuno_apps.helper.helper.Statics;

import java.util.HashSet;
import java.util.Vector;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XPermissionGranter {
    public static final boolean DEBUBG = false;

    private static final Vector<PermissionDemander> s_demanderList = new Vector<>();
    private static boolean s_tilt = false;

    // EVENT

    public XPermissionGranter() {
    }

    // PUBLIC

    public static synchronized void demandPermission(PermissionDemander demander) {
        XDebug.log("PermissionDemander() package=" + demander.packageName, DEBUBG);

        if (!s_tilt) {
            s_demanderList.add(demander);
        }
    }

    public static synchronized void grantDemandedPermissions(ClassLoader classloader) {
        XDebug.log("grantDemandedPermissions() packages=" + s_demanderList.size(), DEBUBG);

        if (!s_tilt) {
            s_tilt = true;

            if (s_demanderList.size() > 0) {
                try {
                    Class packageManagerService = XposedHelpers.findClass(Statics.PACKAGE_NAME__MANAGER_SERVICE, classloader);

                    XposedHelpers.findAndHookMethod(packageManagerService, "grantPermissionsLPw", getParams());
                } catch (Throwable t) {
                    XDebug.log(t);
                }
            }
        } else {
            XDebug.log("grantDemandedPermissions() hook already placed", true);
        }
    }

    // PRIVATE

    private static Object[] getParams() {
        Object[] params;

        XC_MethodHook hook = new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodhookparam)
                    throws Throwable {
                XPermissionGranter.hookPermissions(methodhookparam);
            }
        };

        if (Statics.VERSION__PRE_LOLLIPOP) {
            params = new Object[]{Statics.PACKAGE_NAME__PACKAGE_PARSER_PACKAGE, Boolean.TYPE, hook};
        } else {
            params = new Object[]{Statics.PACKAGE_NAME__PACKAGE_PARSER_PACKAGE, Boolean.TYPE, String.class, hook};
        }

        return params;
    }

    private static void hookPermissions(XC_MethodHook.MethodHookParam param) {
        final String packageName = (String) XposedHelpers.getObjectField(param.args[0], "packageName");

        for (PermissionDemander demander : s_demanderList) {
            if (packageName.equals(demander.packageName)) {
                XDebug.log("PermissionGranter() package=" + packageName, DEBUBG);

                PermissionHelper ph = new PermissionHelper(param);

                for (String permission : demander.permissions) {
                    ph.grantPermission(permission);
                }
            }
        }
    }

    // CLASSES

    public static class PermissionDemander {
        public final String packageName;

        public final Vector<String> permissions = new Vector<>();

        // EVENT

        public PermissionDemander(String packageName) {
            this.packageName = packageName;
        }

        // PUBLIC

        public synchronized void push(String... permissions) {
            for (String permission : permissions) {
                if (!this.permissions.contains(permission)) {
                    this.permissions.add(permission);
                }
            }
        }
    }

    private static class PermissionHelper {
        private final Object context;
        private final Object extras;
        private final HashSet grantedPermissions;
        private final Object permissions;

        // EVENT

        public PermissionHelper(XC_MethodHook.MethodHookParam param) {
            XDebug.log("PermissionHelper()", DEBUBG);

            context = param.thisObject;

            extras = XposedHelpers.getObjectField(param.args[0], "mExtras");
            grantedPermissions = (HashSet) XposedHelpers.getObjectField(extras, "grantedPermissions");
            permissions = XposedHelpers.getObjectField(XposedHelpers.getObjectField(param.thisObject, "mSettings"), "mPermissions");
        }

        // PUBLIC

        public void grantPermission(String new_permission) {
            if (!grantedPermissions.contains(new_permission)) {
                XDebug.log(" + permission '" + new_permission + "' granted", DEBUBG);

                Object permission = XposedHelpers.callMethod(permissions, "get", new_permission);
                //noinspection unchecked
                grantedPermissions.add(new_permission);

                int[] ret = (int[]) XposedHelpers.callStaticMethod(context.getClass(), "appendInts", getParams(permission));
            } else {
                XDebug.log(" ~ permission '" + new_permission + "' already granted", DEBUBG);
            }
        }

        // PRIVATE

        private Object[] getParams(Object permission) {
            return new Object[]{
                    (int[]) XposedHelpers.getObjectField(extras, "gids"),
                    (int[]) XposedHelpers.getObjectField(permission, "gids")
            };
        }
    }
}