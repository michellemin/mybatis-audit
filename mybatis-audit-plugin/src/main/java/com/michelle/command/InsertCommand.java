package com.michelle.command;

import com.michelle.executor.AuditExecutor;
import com.michelle.utils.AuditException;
import org.apache.ibatis.mapping.MappedStatement;

public class InsertCommand extends AbstractAuditCommand {
    public InsertCommand(boolean async, boolean commit, AuditExecutor auditExecutor, MappedStatement mappedStatement, Object parameter) {
        super(async, commit, auditExecutor, mappedStatement, parameter);
    }

    @Override
    public void execute() {

    }
}
