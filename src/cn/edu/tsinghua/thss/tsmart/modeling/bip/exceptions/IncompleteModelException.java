package cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions;

public class IncompleteModelException extends EdolaModelingException {

    private static final long serialVersionUID = 44989751719267844L;

    public IncompleteModelException() {
        super();
    }

    public IncompleteModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteModelException(String message) {
        super(message);
    }

    public IncompleteModelException(Throwable cause) {
        super(cause);
    }
    
}
