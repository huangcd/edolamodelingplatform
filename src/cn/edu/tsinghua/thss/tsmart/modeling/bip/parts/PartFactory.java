package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-22 下午03:43:56
 * @project: CereusBip
 * @package: cereusbip.parts
 * @class: PartFactory.java
 * 
 *         Match the given Model with an EditPart
 */
public class PartFactory implements EditPartFactory {
    private final static String      modelPackage =
                                                                  "cn\\.edu\\.tsinghua\\.thss\\.tsmart\\.modeling\\.bip\\.models\\.";
    private static Pattern           modelPattern = Pattern.compile(modelPackage
                                                                  + "([A-Za-z0-9_]*)Model");

    private final static PartFactory instance     = new PartFactory();

    private PartFactory() {}

    public static PartFactory getInstance() {
        return instance;
    }

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart part = getPartFromModel(model);
        part.setModel(model);
        return part;
    }

    /**
     * 利用反射的方法获得一个Model对应的EditPart，要求在实现的时候， 每一个cereusbip.model.(ABC)Model类必须对应一个
     * cereusbip.parts.(ABC)EditPart类。
     * 
     * @param model
     * @return
     */
    private EditPart getPartFromModel(Object model) {
        String name = model.getClass().getName();
        Matcher matcher = modelPattern.matcher(name);
        if (matcher.matches()) {
            String partName =
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.parts." + matcher.group(1)
                                            + "EditPart";
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
        throw new RuntimeException("Cannot create EditPart for model: "
                        + ((model == null) ? "null" : model.getClass().getName()));
    }
}
