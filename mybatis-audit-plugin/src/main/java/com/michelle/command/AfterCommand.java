package com.michelle.command;


import com.michelle.executor.AuditExecutor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author michelle.min
 */
public class AfterCommand extends AbstractAuditCommand {

    public AfterCommand(boolean async, boolean commit, AuditExecutor auditExecutor, MappedStatement mappedStatement, Object parameter) {
        super(async, commit, auditExecutor, mappedStatement, parameter);
    }

    @Override
    public Object execute() {
        //TODO query
    //    auditExecutor.query(mappedStatement,parameter,RowBounds.DEFAULT,null);
        //insert result
        return null;
    }
}
