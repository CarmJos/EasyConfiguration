package cc.carm.lib.configuration.hocon.exception;

public class HOCONGetValueException extends RuntimeException {
    public HOCONGetValueException() {
        super();
    }

    public HOCONGetValueException(String message) {
        super(message);
    }

    public HOCONGetValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public HOCONGetValueException(Throwable cause) {
        super(cause);
    }
}
