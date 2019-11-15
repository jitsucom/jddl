package ai.ksense.jddl;


/**
 * Generic JDDL exception
 */
public class JDDLException extends RuntimeException {
    public JDDLException(String message) {
        super(message);
    }

    public JDDLException(String message, Throwable cause) {
        super(message, cause);
    }

    public JDDLException(Throwable cause) {
        super(cause.getMessage() == null ? cause.getClass().getName() : cause.getMessage(), cause);
    }
}
