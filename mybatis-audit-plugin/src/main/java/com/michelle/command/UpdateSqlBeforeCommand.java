package com.michelle.command;


import com.michelle.executor.AuditExecutor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author michelle.min
 */
public class UpdateSqlBeforeCommand extends AbstractAuditCommand {

    public UpdateSqlBeforeCommand(boolean async, boolean commit, AuditExecutor auditExecutor, MappedStatement mappedStatement, Object parameter) {
        super(async, commit, auditExecutor, mappedStatement, parameter);
    }

    @Override
    public Object execute() {
        return null;
    }
}
