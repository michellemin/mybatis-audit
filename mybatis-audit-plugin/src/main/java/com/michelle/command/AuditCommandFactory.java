package com.michelle.command;


import com.michelle.builder.MappedStatementBuilder;

/**
 * @author michelle.min
 */
public interface AuditCommandFactory {
    AuditCommand createBeforeCommand(MappedStatementBuilder mappedStatementBuilder);

    AuditCommand createAfterCommand(MappedStatementBuilder mappedStatementBuilder);

    AuditCommand createInsertCommand(MappedStatementBuilder mappedStatementBuilder);
}
