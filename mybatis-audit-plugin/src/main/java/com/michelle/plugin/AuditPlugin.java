package com.michelle.plugin;

import com.michelle.mapped.MappedStatementBuilder;
import com.michelle.command.*;
import com.michelle.command.factory.AuditCommandFactory;
import com.michelle.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.util.*;


/**
 * @author michelle.min
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class AuditPlugin implements Interceptor {
    private Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getFromProperty(String key) {
        return properties != null ? properties.getProperty(key) : null;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnObject;
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Executor executor = (Executor) invocation.getTarget();
        MappedStatementBuilder mappedStatementBuilder = null;
        AuditCommandFactory auditCommandFactory = null;
        try {
            mappedStatementBuilder = new MappedStatementBuilder(mappedStatement, parameter);
            auditCommandFactory = AuditCommandFactory.instance(mappedStatement.getSqlCommandType());
        } catch (AuditObjectNotFoundException e1) {
            log.debug("Can't find audit object", e1);
        } catch (Exception e2) {
            log.debug("Create AuditCommandFactory and MappedStatementBuilder error", e2);
        }
        boolean isAudit = mappedStatementBuilder != null && auditCommandFactory != null;
        Object beforeResult;
        if (isAudit) {
            try {
                AuditCommand beforeCommand = auditCommandFactory.createBeforeCommand(mappedStatementBuilder);
                beforeResult = beforeCommand.execute();
            } catch (Exception e) {
                log.error("BeforeCommand error", e);
            }
        }
        returnObject = invocation.proceed();
        if (isAudit) {
            try {
                AuditCommand afterCommand = auditCommandFactory.createAfterCommand(mappedStatementBuilder);
                if (AuditCommandThreadLocal.get() != null) {
                    AuditCommandThreadLocal.add(afterCommand);
                } else {
                    afterCommand.execute();
                }
            } catch (Exception e) {
                log.error("AfterCommand error", e);
            }
        }

        return returnObject;
    }

    private Object getAfterParameter(Object returnObject, Object beforeResult, SqlCommandType sqlCommandType) {
        Object afterParameter;
        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
            if (returnObject instanceof Map) {
                returnObject = ((Map) returnObject).get("list");
            }
            if (returnObject instanceof Collection) {
                afterParameter = new ArrayList<>((Collection<Object>) returnObject);
            } else if (returnObject != null) {
                afterParameter = Collections.singletonList(returnObject);
            }
        } else {
            afterParameter = beforeResult;
        }
        return null;
    }


}
