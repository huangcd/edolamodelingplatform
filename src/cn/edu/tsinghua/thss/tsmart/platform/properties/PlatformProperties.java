package cn.edu.tsinghua.thss.tsmart.platform.properties;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import cn.edu.tsinghua.thss.tsmart.modeling.validation.Validator;

public interface PlatformProperties {
    String DATA_TYPE_FILE      = "datatype";
    String PORT_TYPE_FILE      = "porttype";
    String CONNECTOR_TYPE_FILE = "connectortype";
    String ATOMIC_TYPE_FILE    = "atomictype";
    String COMPOUND_TYPE_FILE  = "compoundtype";
    String ATOMIC_EXTENSION    = "edolaa";
    String COMPOUND_EXTENSION  = "edolam";
    String ZIP_EXTENSION       = "edolaz";

    Font getDefaultEditorFont();

    Color getPlaceLabelColor();

    Color getActionLabelColor();

    Color getGuardLabelColor();

    Color getConnectorColor();

    Color getPortLabelColor();

    Color getDataLabelColor();

    Font getDefaultEditorBoldFont();

    List<Validator> getValidators();
}
