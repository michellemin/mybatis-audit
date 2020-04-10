package com.michelle.command.factory;


import com.michelle.mapped.MappedStatementBuilder;
import com.michelle.mapped.parameter.ParameterBuilder;
import com.michelle.command.AfterCommand;
import com.michelle.command.AuditCommand;
import com.michelle.command.InsertCommand;
import com.michelle.command.InsertSqlBeforeCommand;
import com.michelle.utils.AuditCommandType;

/**
 * @author michelle.min
 */
public class InsertSqlCommandFactory implements AuditCommandFactory {
    private static InsertSqlCommandFactory singleton = null;

    private InsertSqlCommandFactory() {
    }

    static InsertSqlCommandFactory createSingleton() {
        if (singleton == null) {
            singleton = new InsertSqlCommandFactory();
        }
        return singleton;
    }

    @Override
    public AuditCommand createBeforeCommand(MappedStatementBuilder mappedStatementBuilder) {
        return new InsertSqlBeforeCommand();
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
