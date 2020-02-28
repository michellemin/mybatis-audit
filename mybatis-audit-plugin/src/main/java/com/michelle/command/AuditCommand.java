package com.michelle.command;


import com.michelle.utils.AuditException;

/**
 * @author michelle.min
 */
public interface AuditCommand {
    void execute() throws AuditException;
}
