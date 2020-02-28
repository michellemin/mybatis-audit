package com.michelle.utils;


/**
 * @author michelle.min
 */
public class AuditException extends Exception {
    public AuditException() {
    }

    public AuditException(String message) {
        super(message);
    }

    public AuditException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuditException(Throwable cause) {
        super(cause);
    }
}
