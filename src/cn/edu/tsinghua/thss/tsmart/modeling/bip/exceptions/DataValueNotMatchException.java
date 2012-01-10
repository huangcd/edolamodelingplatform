package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class DataValueNotMatchException extends EdolaModelingException {
    private static final long serialVersionUID = 8938321156211518931L;

    public DataValueNotMatchException(String type, String value) {
        super(String.format("A data with type %s cannot has value of %s", type, value));
    }
}
