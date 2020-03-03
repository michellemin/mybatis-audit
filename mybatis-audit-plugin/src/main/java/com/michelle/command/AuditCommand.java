package com.michelle.command;


import com.michelle.utils.AuditException;

/**
 * @author michelle.min
 */
public interface AuditCommand {
    Object execute() throws AuditException;
}
