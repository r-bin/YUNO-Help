package org.yuno_apps.helper.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import org.yuno_apps.helper.debug.XDebug;

import java.util.Vector;

public class XIconHider {
    public static final boolean DEBUG = true;

    private final Context m_context;
    private final PackageManager m_packageManager;

    private final Vector<ComponentName> m_list = new Vector<>();

    // EVENT

    public XIconHider(final Context context) {
        m_context = context;
        m_packageManager = context.getPackageManager();
    }

    // PUBLIC

    public void addComponent(final String... componentNames) {
        for (final String componentName : componentNames) {
            if (componentName != null) {
                final ComponentName component = new ComponentName(m_context, componentName);

                if (!m_list.contains(component)) {
                    XDebug.log(this.getClass(), "Adding component=" + componentName, true);

                    m_list.add(component);
                } else {
                    XDebug.log(this.getClass(), "Already added component=" + componentName, true);
                }
            } else {
                XDebug.log(this.getClass(), "Empty component could not be added", true);
            }
        }
    }

    public void show() {
        setComponentVisibility(m_context, true);
    }

    public void hide() {
        setComponentVisibility(m_context, false);
    }

    // PRIVATE

    private void setComponentVisibility(final Context context, final boolean visible) {
        XDebug.log(this.getClass(), "Setting visibility=" + visible, DEBUG);

        for (final ComponentName component : m_list) {
            try {
                m_packageManager.setComponentEnabledSetting(component, getState(visible), PackageManager.DONT_KILL_APP);

                XDebug.log(this.getClass(), "Icon hidden! component=" + component.getClassName(), DEBUG);
            } catch (Exception e) {
                XDebug.log(this.getClass(), e);
                XDebug.log(this.getClass(), "Icon not found. component=" + component.getClassName(), DEBUG);
            }
        }
    }

    private int getState(final boolean visible) {
        if (visible) {
            return PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        } else {
            return PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        }
    }
}
