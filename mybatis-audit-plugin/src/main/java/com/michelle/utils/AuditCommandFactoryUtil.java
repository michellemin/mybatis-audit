package com.michelle.utils;


import com.michelle.command.AuditCommandFactory;
import com.michelle.command.InsertSqlCommandFactory;
import com.michelle.command.UpdateSqlCommandFactory;
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
                    auditCommandFactory = new InsertSqlCommandFactory();
                    break;
                case UPDATE:
                    auditCommandFactory = new UpdateSqlCommandFactory();
                    break;
                default:

            }
        }
        return auditCommandFactory;
    }
}
