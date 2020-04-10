package com.michelle.command;


import com.michelle.executor.AuditExecutor;
import com.michelle.utils.AuditException;
import com.michelle.utils.AuditRuntimeException;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michelle.min
 */
public class BatchCommand extends AbstractAuditCommand {
    private List<AuditCommand> children = new ArrayList<>();

    public BatchCommand(boolean async, boolean commit, AuditExecutor auditExecutor, MappedStatement mappedStatement, Object parameter) {
        super(async, commit, auditExecutor, mappedStatement, parameter);
    }

    @Override
    public Object execute() throws AuditException {
        for (AuditCommand auditCommand : children) {
            auditCommand.execute();
        }
        return null;
    }

    @Override
    public boolean add(AuditCommand auditCommand) {
        if (auditCommand == null) {
            throw new AuditRuntimeException("Audit command is null");
        }
        return children.add(auditCommand);
    }

    @Override
    public AuditCommand remove(int index) {
        return children.remove(index);
    }

    @Override
    public AuditCommand getChild(int index) {
        return children.get(index);
    }
}
