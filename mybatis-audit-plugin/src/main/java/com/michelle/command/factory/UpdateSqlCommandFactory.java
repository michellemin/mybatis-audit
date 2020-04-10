package com.michelle.command.factory;


import com.michelle.mapped.MappedStatementBuilder;
import com.michelle.mapped.parameter.ParameterBuilder;
import com.michelle.command.AfterCommand;
import com.michelle.command.AuditCommand;
import com.michelle.command.InsertCommand;
import com.michelle.utils.AuditCommandType;

/**
 * @author michelle.min
 */
public class UpdateSqlCommandFactory implements AuditCommandFactory {
    private static UpdateSqlCommandFactory singleton = null;

    private UpdateSqlCommandFactory() {
    }

    static UpdateSqlCommandFactory createSingleton() {
        if (singleton == null) {
            singleton = new UpdateSqlCommandFactory();
        }
        return singleton;
    }

    @Override
    public AuditCommand createBeforeCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new AfterCommand(
                true,
                true,
                null,
                mappedStatementBuilder.sqlSource(null, AuditCommandType.BEFORE).build(),
                ParameterBuilder.build());
    }

    @Override
    public AuditCommand createAfterCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new AfterCommand(
                true,
                true,
                null,
                mappedStatementBuilder.sqlSource(null, AuditCommandType.AFTER).build(),
                ParameterBuilder.build());
    }

    @Override
    public AuditCommand createInsertCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new InsertCommand(
                true,
                true,
                null,
                mappedStatementBuilder.sqlSource(null, AuditCommandType.INSERT).build(),
                ParameterBuilder.build());
    }
}
