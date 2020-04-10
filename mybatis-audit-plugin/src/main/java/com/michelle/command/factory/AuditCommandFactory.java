package com.michelle.command.factory;


import com.michelle.builder.MappedStatementBuilder;
import com.michelle.command.AuditCommand;

/**
 * @author michelle.min
 */
public interface AuditCommandFactory {
    AuditCommand createBeforeCommand(MappedStatementBuilder mappedStatementBuilder);

    AuditCommand createAfterCommand(MappedStatementBuilder mappedStatementBuilder);

    AuditCommand createInsertCommand(MappedStatementBuilder mappedStatementBuilder);
}
