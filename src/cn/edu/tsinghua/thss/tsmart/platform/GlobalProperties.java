package cn.edu.tsinghua.thss.tsmart.platform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.baseline.BaselineDataAccessor;
import cn.edu.tsinghua.thss.tsmart.modeling.ModelingProperties;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.CompareRelationValidator;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.ConnectRelationValidator;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.HasRelationValidator;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.Rule;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.Validator;

@Root
public class GlobalProperties implements ModelingProperties, PlatformProperties {
    @Attribute(name = "modelchecking")
    private boolean                 enableModelChecking     = false;
    @Attribute(name = "codegeneration")
    private boolean                 enableCodeGeneration    = false;
    @Element
    private String                  defaultEditorFontName   = "Consolas";
    @Element
    private int                     defaultEditorFontHeight = 10;
    @Element
    private int                     defaultEditorFontStyle  = SWT.BOLD;
    @Attribute(name = "atomicpriority")
    private boolean                 allowAtomicPriority     = false;
    @Attribute(name = "priority")
    private boolean                 allowPriority           = false;
    @Attribute(name = "broadcast")
    private boolean                 allowBroadcast          = false;

    private Color                   placeLabelColor         = ColorConstants.blue;
    private Color                   actionLabelColor        = new Color(null, 204, 102, 255);
    private Color                   guardLabelColor         = new Color(null, 100, 180, 100);
    private Color                   connectorColor          = new Color(null, 99, 37, 35);
    private Color                   connectorOutlineColor   = new Color(null, 113, 137, 63);
    private Color                   connectorFillColor      = new Color(null, 155, 187, 89);
    private Color                   connectionLabelColor    = new Color(null, 0, 51, 0);
    private Color                   atomicLabelColor        = new Color(null, 60, 60, 60);
    private Color                   compoundLabelColor      = new Color(null, 13, 13, 13);
    private Color                   portLabelColor          = ColorConstants.lightBlue;
    private Color                   dataLabelColor          = ColorConstants.darkGray;
    private List<Validator>         validators;
    private Font                    defaultEditorFont;
    private static GlobalProperties instance;
    private static ArrayList<String> keywords;

    public final static GlobalProperties getInstance() {
        if (instance == null) {
            instance = new GlobalProperties();
        }
        return instance;
    }

    private GlobalProperties() {
        validators = new ArrayList<Validator>();
        validators.add(HasRelationValidator.getInstance());
        validators.add(CompareRelationValidator.getInstance());
        validators.add(ConnectRelationValidator.getInstance());
        keywords=new ArrayList<String>();
        keywords.add("place");
    }

    public boolean isMultipleDataTypeAvailble() {
        return !(enableModelChecking || enableCodeGeneration);
    }

    @Override
    public boolean isAtomicPriorityAllow() {
        return allowAtomicPriority;
    }

    @Override
    public boolean isPriorityAllow() {
        return allowPriority;
    }

    @Override
    public boolean isBroadcastAllow() {
        return allowBroadcast;
    }

    @Override
    public Font getDefaultEditorFont() {
        if (defaultEditorFont == null) {
            Device device = Display.getCurrent();
            defaultEditorFont =
                            new Font(device, new FontData(defaultEditorFontName,
                                            defaultEditorFontHeight, defaultEditorFontStyle));
        }
        return defaultEditorFont;
    }

    @Override
    public Font getDefaultEditorBoldFont() {
        return new Font(Display.getCurrent(), new FontData(defaultEditorFontName,
                        defaultEditorFontHeight, SWT.BOLD));
    }

    @Override
    public Color getPlaceLabelColor() {
        return placeLabelColor;
    }

    @Override
    public Color getActionLabelColor() {
        return actionLabelColor;
    }

    @Override
    public Color getGuardLabelColor() {
        return guardLabelColor;
    }

    @Override
    public Color getConnectorColor() {
        return connectorColor;
    }

    @Override
    public Color getPortLabelColor() {
        return portLabelColor;
    }

    public Color getDataLabelColor() {
        return dataLabelColor;
    }

    public Color getConnectorOutlineColor() {
        return connectorOutlineColor;
    }

    public Color getConnectorFillColor() {
        return connectorFillColor;
    }

    public Color getConnectionLabelColor() {
        return connectionLabelColor;
    }

    public Color getAtomicLabelColor() {
        return atomicLabelColor;
    }

    public Color getCompoundLabelColor() {
        return compoundLabelColor;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public List<Rule> getRules() {
        try {
            BaselineDataAccessor bda = new BaselineDataAccessor();
            java.util.List<cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule> arules =
                            bda.getRules(bda.getBaselines().get(0).getName());
            java.util.List<Rule> ruleList = new ArrayList<Rule>();
            for (Iterator<cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule> iterator =
                            arules.iterator(); iterator.hasNext();) {
                cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule arule =
                                (cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule) iterator
                                                .next();
                Rule rule = new Rule();
                rule.setFirstEntity(arule.getSubjectEntityName());
                rule.setSecondEntity(arule.getObjectEntityName());
                rule.setRelation(arule.getRelation());
                rule.setMax(arule.getMax());
                rule.setMin(arule.getMin());
            }
            return ruleList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<Rule>();
        }
    }

    public void registerValidator(Validator validator) {
        validators.add(validator);
    }
    public static ArrayList<String> getKeywords() {
        return keywords;
    }
}
