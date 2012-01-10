package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;


/**
 * 顶层模型，只能是ProjectModel或者LibraryModel
 * 
 * @author Huangcd
 */
@SuppressWarnings("rawtypes")
public interface ITopModel<Model extends ITopModel> extends IContainer<Model, IInstance, ITopModel, IType> {}
