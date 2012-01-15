package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TreeEditPartFactory implements EditPartFactory {
    private static final String              PartNamePattern;
    private final static Pattern             modelPattern;
    private final static TreeEditPartFactory instance;

    static {
        PartNamePattern = "cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline.{0}TreeEditPart";
        modelPattern =
                        Pattern.compile("cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.([A-Za-z0-9_]*)Model");
        instance = new TreeEditPartFactory();
    }

    private TreeEditPartFactory() {}

    public static TreeEditPartFactory getInstance() {
        return instance;
    }

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart part = getPartFromModel(model);
        if (part == null) {
            System.out.println("Part is null for model" + model);
        }
        if (model == null) {
            System.out.println("Model is null");
        }
        if (part != null && model != null) part.setModel(model);
        return part;
    }

    /**
     * 利用反射的方法获得一个Model对应的EditPart，要求在实现的时候， 每一个cereusbip.model.(ABC)Model类必须对应一个
     * cereusbip.parts.(ABC)TreeEditPart类。
     * 
     * @param model
     * @return
     */
    private EditPart getPartFromModel(Object model) {
        EditPart part = null;
        String name = model.getClass().getName();
        Matcher matcher = modelPattern.matcher(name);
        if (matcher.matches()) {
            String partName = MessageFormat.format(PartNamePattern, matcher.group(1));
            try {
                return (EditPart) Class.forName(partName).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                ErrorDialog.openError(new Shell(Display.getDefault()), "ClassNotFound",
                                "You should have a class named '" + partName + "'",
                                Status.OK_STATUS);
            }
        }
        return part;
    }
}
