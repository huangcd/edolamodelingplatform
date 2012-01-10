package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class EdolaModelingException extends RuntimeException {

    private static final long serialVersionUID = 6376729249547782898L;

    public EdolaModelingException() {
        super();
    }

    public EdolaModelingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EdolaModelingException(String message) {
        super(message);
    }

    public EdolaModelingException(Throwable cause) {
        super(cause);
    }
}
