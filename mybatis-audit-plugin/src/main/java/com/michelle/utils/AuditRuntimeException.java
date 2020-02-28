package com.michelle.utils;


/**
 * @author michelle.min
 */
public class AuditRuntimeException extends RuntimeException {
    public AuditRuntimeException() {
    }

    public AuditRuntimeException(String message) {
        super(message);
    }

    public AuditRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuditRuntimeException(Throwable cause) {
        super(cause);
    }

}
