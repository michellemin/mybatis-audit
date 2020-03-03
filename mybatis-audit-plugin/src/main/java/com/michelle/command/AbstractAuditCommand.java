package com.michelle.command;


import com.michelle.executor.AuditExecutor;
import com.michelle.utils.AuditException;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author michelle.min
 */
public abstract class AbstractAuditCommand implements AuditCommand {
    protected boolean async;
    protected boolean commit;
    protected AuditExecutor auditExecutor;
    protected Object result;
    protected MappedStatement mappedStatement;
    protected Object parameter;

    public AbstractAuditCommand(boolean async, boolean commit, AuditExecutor auditExecutor, MappedStatement mappedStatement, Object parameter) {
        this.async = async;
        this.commit = commit;
        this.auditExecutor = auditExecutor;
        this.mappedStatement = mappedStatement;
        this.parameter = parameter;
    }

    @Override
    public abstract Object execute() throws AuditException;
}
