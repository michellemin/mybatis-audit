package com.michelle.utils;


import com.michelle.command.factory.AuditCommandFactory;
import com.michelle.command.factory.InsertSqlCommandFactory;
import com.michelle.command.factory.UpdateSqlCommandFactory;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author michelle.min
 */
public class AuditCommandFactoryUtil {
    private AuditCommandFactoryUtil() {

    }

    public static AuditCommandFactory createAuditCommandFactory(SqlCommandType sqlCommandType) {
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
}
