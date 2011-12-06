package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;


// test by linying
public class CustomDirectEditManager extends DirectEditManager {

    private PlaceModel       placeModel;        // 要修改该模型的文本属性
    private TransitionModel  transitionModel;
    private AtomicTypeModel  atomicTypeModel;
    private DataModel        dataModel;
    private PortModel        portModel;
    private PriorityModel    priorityModel;

    private int              choice         = 0;
    private static final int EDITPLACE      = 1;
    private static final int EDITTRANSITION = 2;
    private static final int EDITATOMIC     = 3;
    private static final int EDITDATA       = 4;
    private static final int EDITPORT       = 5;
    private static final int EDITPRIORITY   = 6;

    @SuppressWarnings("rawtypes")
    public CustomDirectEditManager(GraphicalEditPart source, Class editorType,
                    CellEditorLocator locator) {
        super(source, editorType, locator);
        // 获得 模型
        if (source instanceof PlaceEditPart) {
            placeModel = (PlaceModel) source.getModel();
            choice = CustomDirectEditManager.EDITPLACE;
        } else if (source instanceof TransitionEditPart) {
            transitionModel = (TransitionModel) source.getModel();
            choice = CustomDirectEditManager.EDITTRANSITION;
        } else if (source instanceof AtomicTypeEditPart) {
            atomicTypeModel = (AtomicTypeModel) source.getModel();
            choice = CustomDirectEditManager.EDITATOMIC;
        } else if (source instanceof DataEditPart) {
            dataModel = (DataModel) source.getModel();
            choice = CustomDirectEditManager.EDITDATA;
        } else if (source instanceof PortEditPart) {
            portModel = (PortModel) source.getModel();
            choice = CustomDirectEditManager.EDITPORT;
        } else if (source instanceof PriorityEditPart) {
            priorityModel = (PriorityModel) source.getModel();
            choice = CustomDirectEditManager.EDITPRIORITY;
        }
    }

    @Override
    protected void initCellEditor() {
        // 在显示一个 cell editor 之前，先给它设置一个值
        // 这里的值是获得图形模型的文本
        if (choice == EDITPLACE)
            getCellEditor().setValue(placeModel.getName());
        else if (choice == EDITTRANSITION)
            getCellEditor().setValue(transitionModel.toString());
        else if (choice == EDITATOMIC)
            getCellEditor().setValue(atomicTypeModel.getName());
        else if (choice == EDITDATA)
            getCellEditor().setValue(dataModel.toString());
        else if (choice == EDITPORT)
            getCellEditor().setValue(portModel.toString());
        else if (choice == EDITPRIORITY) getCellEditor().setValue(priorityModel.toString());

        // 在所选中的 TextCellEditor 的 Text 控件中的所有文本都显示为选择状态
        Text text = (Text) getCellEditor().getControl();
        text.selectAll();
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
