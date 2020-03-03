package com.michelle.command;


import com.michelle.builder.MappedStatementBuilder;
import com.michelle.builder.ParameterBuilder;
import com.michelle.utils.AuditCommandType;

/**
 * @author michelle.min
 */
public class UpdateSqlCommandFactory implements AuditCommandFactory {
    @Override
    public AuditCommand createBeforeCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new AfterCommand(
                true,
                true,
                null,
                mappedStatementBuilder.buildSqlSource(null, AuditCommandType.BEFORE).build(),
                ParameterBuilder.build());
    }

    @Override
    public AuditCommand createAfterCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new AfterCommand(
                true,
                true,
                null,
                mappedStatementBuilder.buildSqlSource(null, AuditCommandType.AFTER).build(),
                ParameterBuilder.build());
    }

    @Override
    public AuditCommand createInsertCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new InsertCommand(
                true,
                true,
                null,
                mappedStatementBuilder.buildSqlSource(null, AuditCommandType.INSERT).build(),
                ParameterBuilder.build());
    }
}
