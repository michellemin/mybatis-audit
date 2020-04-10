package com.michelle.command;


import com.michelle.utils.AuditException;
import com.michelle.utils.AuditRuntimeException;

/**
 * @author michelle.min
 */
public interface AuditCommand {
    Object execute() throws AuditException;

    default boolean add(AuditCommand auditCommand) {
        throw new AuditRuntimeException("Invalid operation!");
    }

    default AuditCommand remove(int index) {
        throw new AuditRuntimeException("Invalid operation!");
    }

    default AuditCommand getChild(int index) {
        throw new AuditRuntimeException("Invalid operation!");
    }
}
