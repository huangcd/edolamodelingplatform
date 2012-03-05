package cn.edu.tsinghua.thss.tsmart.platform;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "EdolaModelingPlatform"; //$NON-NLS-1$

    // The shared instance
    private static Activator   plugin;

    private static File        preferenceDirection;

    public static File getPreferenceDirection() {
        if (preferenceDirection == null) {
            preferenceDirection = getDefault().getStateLocation().makeAbsolute().toFile();
        }
        return preferenceDirection;
    }

    /**
     * The constructor
     */
    public Activator() {}

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @SuppressWarnings("rawtypes")
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        // 退出的时候自动保存构件库/项目
        TopLevelModel model = GlobalProperties.getInstance().getTopModel();
        if (model != null) {
            model.save();
        }
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
