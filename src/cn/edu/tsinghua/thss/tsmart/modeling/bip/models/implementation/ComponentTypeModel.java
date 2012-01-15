package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.ITopModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

@SuppressWarnings("rawtypes")
public abstract class ComponentTypeModel<Model extends ComponentTypeModel, Instance extends ComponentModel>
                extends BaseTypeModel<Model, Instance, ITopModel>
                implements
                    IContainer<Model, Instance, ITopModel, IInstance>,
                    IType<Model, Instance, ITopModel> {

    private static final long serialVersionUID = -3172665284154222983L;

    /**
     * @return 返回export port的集合（构件对外可见的端口集）
     */
    public abstract List<PortModel> getExportPorts();

    public abstract boolean checkExistenceByName(String name);

    public abstract String getHardwareSoftwareType();

    @Override
    public void ensureUniqueName(IInstance child) {
        HashSet<String> typeNames = new HashSet<String>();
        HashSet<String> names = new HashSet<String>();
        String name = child.getName();
        for (IInstance model : getChildren()) {
            names.add(model.getName());
            if (model.getType() instanceof ComponentTypeModel) {
                typeNames.add(model.getType().getName());
            }
        }
        while (names.contains(name)) {
            Matcher mat = NAME_PREFIX.matcher(name);
            if (mat.matches()) {
                String baseName = mat.group(1);
                String number = mat.group(2);
                name = baseName + "" + (Integer.parseInt(number) + 1);
            } else {
                name = name + "1";
            }
        }
        if (child instanceof ComponentModel) {
            String typeName = child.getType().getName();
            while (typeNames.contains(typeName)) {
                Matcher mat = NAME_PREFIX.matcher(typeName);
                if (mat.matches()) {
                    String baseName = mat.group(1);
                    String number = mat.group(2);
                    typeName = baseName + "" + (Integer.parseInt(number) + 1);
                } else {
                    typeName = typeName + "1";
                }
            }
            child.getType().setName(typeName);
        }
        child.setName(name);
    }
}
