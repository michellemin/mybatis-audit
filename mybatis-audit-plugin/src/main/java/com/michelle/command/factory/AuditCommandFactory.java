package com.michelle.command.factory;


import com.michelle.mapped.MappedStatementBuilder;
import com.michelle.command.AuditCommand;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author michelle.min
 */
public interface AuditCommandFactory {

    static AuditCommandFactory instance(SqlCommandType sqlCommandType) {
        AuditCommandFactory auditCommandFactory = null;
        if (sqlCommandType != null) {
            switch (sqlCommandType) {
                case INSERT:
                    auditCommandFactory = InsertSqlCommandFactory.createSingleton();
                    break;
                case UPDATE:
                    auditCommandFactory = UpdateSqlCommandFactory.createSingleton();
                    break;
                default:

            }
        }
        return auditCommandFactory;
    }

    AuditCommand createBeforeCommand(MappedStatementBuilder mappedStatementBuilder);

    AuditCommand createAfterCommand(MappedStatementBuilder mappedStatementBuilder);

    AuditCommand createInsertCommand(MappedStatementBuilder mappedStatementBuilder);
}
