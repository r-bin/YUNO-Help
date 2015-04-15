package org.yuno_apps.helper.helper;

import android.os.Build;

public class Statics {
    public static final String PACKAGE_NAME__MANAGER_SERVICE = "com.android.server.pm.PackageManagerService";
    public static final String PACKAGE_NAME__PACKAGE_PARSER_PACKAGE = "android.content.pm.PackageParser.Package";

    public static final boolean VERSION__PRE_LOLLIPOP = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
}
