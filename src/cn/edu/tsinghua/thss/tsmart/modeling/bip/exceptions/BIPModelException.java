package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class BIPModelException extends RuntimeException {

    public BIPModelException() {
        super();
    }

    public BIPModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public BIPModelException(String message) {
        super(message);
    }

    public BIPModelException(Throwable cause) {
        super(cause);
    }
}
