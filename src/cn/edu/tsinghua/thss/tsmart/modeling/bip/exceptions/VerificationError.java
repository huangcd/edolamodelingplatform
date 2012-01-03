package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class VerificationError extends BIPModelingException {
    private static final long serialVersionUID = 7135066354984620659L;

    public VerificationError() {
        super();
    }

    public VerificationError(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationError(String message) {
        super(message);
    }

    public VerificationError(Throwable cause) {
        super(cause);
    }
}
