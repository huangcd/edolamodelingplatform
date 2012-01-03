package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

import java.text.MessageFormat;

public class UnboundedException extends BIPModelingException {

    private static final long serialVersionUID = 339470030982167589L;

    public UnboundedException(String boundType, String boundName, int index) {
        super(MessageFormat.format("The No.{0} of {1} {2} is not bounded", index, boundType,
                        boundName));
    }

    public UnboundedException() {
        super();
    }

    public UnboundedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnboundedException(String message) {
        super(message);
    }

    public UnboundedException(Throwable cause) {
        super(cause);
    }
}
