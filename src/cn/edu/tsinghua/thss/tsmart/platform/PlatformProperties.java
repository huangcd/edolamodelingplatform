package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public interface PlatformProperties {
    Font getDefaultEditorFont();

    Color getPlaceLabelColor();

    Color getActionLabelColor();

    Color getGuardLabelColor();

    Color getPortLabelColor();
}
