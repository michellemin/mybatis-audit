package com.michelle.utils;


/**
 * @author michelle.min
 */
public class AuditObjectNotFoundException extends AuditRuntimeException {
    public AuditObjectNotFoundException() {
    }

    public AuditObjectNotFoundException(String message) {
        super(message);
    }

    public AuditObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuditObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
