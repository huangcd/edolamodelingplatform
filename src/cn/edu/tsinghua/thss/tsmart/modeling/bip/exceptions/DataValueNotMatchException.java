package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class DataValueNotMatchException extends BIPModelException {
    public DataValueNotMatchException(String type, String value) {
        super(String.format("A data with type %s cannot has value of %s", type, value));
    }
}
