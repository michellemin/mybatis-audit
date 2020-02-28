package com.michelle.command;


import com.michelle.builder.MappedStatementBuilder;
import com.michelle.builder.ParameterBuilder;
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
    public void execute() {
        //TODO query

        //insert result
        InsertCommand insertCommand = new InsertCommand(true, true, null, MappedStatementBuilder.build(), ParameterBuilder.build());
    }
}
