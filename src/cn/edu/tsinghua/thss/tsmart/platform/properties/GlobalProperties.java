package cn.edu.tsinghua.thss.tsmart.platform.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Entity;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.CompareRelationValidator;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.ConnectRelationValidator;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.HasRelationValidator;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.Rule;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.Validator;

@Root
@SuppressWarnings({"rawtypes", "unchecked"})
public class GlobalProperties
                implements
                    ModelingProperties,
                    PlatformProperties,
                    BaselineProperties,
                    CodeGenerationProperties {
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

    /**
     * 当前的顶层模型
     */
    private transient TopLevelModel model;

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
    private Set<String>             keywords;
    private ArrayList<Rule>         rules;
    private List<Entity>            entities;
    /*
     * 针对代码生成所需变量，后期需要改进 TODO 初始化为空串
     */

    private static GlobalProperties instance;

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
        keywords = new HashSet<String>();
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

    public void initRuleAndEntitiesFromBaseline(String baseline) {
        rules = new ArrayList<Rule>();
        try {
            BaselineDataAccessor bda = new BaselineDataAccessor();
            String baselineName = bda.getBaselines().get(0).getName();
            java.util.List<cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule> arules =
                            bda.getRules(baselineName);

            for (Iterator<cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule> iterator =
                            arules.iterator(); iterator.hasNext();) {
                cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule arule =
                                (cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Rule) iterator
                                                .next();
                Rule rule = new Rule();
                rule.setFirstEntity(arule.getSubjectEntityName());
                rule.setSecondEntity(arule.getObjectEntityName());
                rule.setRelation(arule.getRelation());
                rule.setNeedCheckOnline(true);
                rule.setMax(arule.getMax());
                rule.setMin(arule.getMin());
                rules.add(rule);
            }
            setEntities(bda.getEntities(baselineName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Rule> getRules() {
        if (rules == null) {
            TopLevelModel model = getTopModel();
            if (model == null) {
                return Collections.EMPTY_LIST;
            }
            initRuleAndEntitiesFromBaseline(model.getBaseline());
        }
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = new ArrayList<Rule>(rules);
    }

    public void registerValidator(Validator validator) {    
        validators.add(validator);
    }

    public boolean isKeyWord(String str) {
        return keywords.contains(str);
    }

    /**
     * @return the entities
     */
    public List<Entity> getEntities() {
        if (entities == null) {
            TopLevelModel model = getTopModel();
            if (model == null) {
                return Collections.EMPTY_LIST;
            }
            initRuleAndEntitiesFromBaseline(model.getBaseline());
        }
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public TopLevelModel getTopModel() {
        return model;
    }

    @Override
    public void setTopModel(TopLevelModel model) {
        this.model = model;
    }

}
