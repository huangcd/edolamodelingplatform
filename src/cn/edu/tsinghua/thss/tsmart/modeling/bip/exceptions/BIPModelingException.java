package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class BIPModelingException extends RuntimeException {

    private static final long serialVersionUID = 6376729249547782898L;

    public BIPModelingException() {
        super();
    }

    public BIPModelingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BIPModelingException(String message) {
        super(message);
    }

    public BIPModelingException(Throwable cause) {
        super(cause);
    }
}
