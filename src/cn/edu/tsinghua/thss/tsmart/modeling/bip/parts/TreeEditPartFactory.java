package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class TreeEditPartFactory implements EditPartFactory {

    private final static String              modelPackage =
                                                                          "cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.";
    private static Pattern                   modelPattern = Pattern.compile(modelPackage
                                                                          + "([A-Za-z0-9_]*)Model");

    private final static TreeEditPartFactory instance     = new TreeEditPartFactory();

    private TreeEditPartFactory() {}

    public static TreeEditPartFactory getInstance() {
        return instance;
    }

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart part = getPartFromModel(model);
        if (part == null) System.out.println("Part is null");
        if (model == null) System.out.println("Model is null");
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
            String partName =
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.parts." + matcher.group(1)
                                            + "TreeEditPart";
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
