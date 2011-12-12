package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class ValidationError extends RuntimeException {

    public ValidationError() {
        super();
    }

    public ValidationError(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationError(String message) {
        super(message);
    }

    public ValidationError(Throwable cause) {
        super(cause);
    }
}
