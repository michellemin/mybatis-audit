package com.michelle.plugin;

import com.michelle.builder.MappedStatementBuilder;
import com.michelle.builder.ParameterBuilder;
import com.michelle.command.AfterCommand;
import com.michelle.command.AuditCommand;
import com.michelle.command.BeforeInsertCommand;
import com.michelle.command.BeforeUpdateCommand;
import com.michelle.utils.AuditCommandType;
import com.michelle.utils.AuditCommandThreadLocal;
import com.michelle.utils.AuditObjectNotFoundException;
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
        AuditCommand beforeCommand = createAuditCommand(mappedStatement, AuditCommandType.BEFORE);
        try {
            beforeCommand.execute();
        } catch (Exception e) {
        }
        returnObject = invocation.proceed();
        try {
            executeAfterCommand(mappedStatement, parameter);
        } catch (Exception e) {

        }

        return returnObject;
    }

    private void executeAfterCommand(MappedStatement mappedStatement, Object parameter) throws Exception {
        //批量插入参数默认为list，从list中获取插入操作返回id
        Object afterParameter;
        if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            if (parameter instanceof Map) {
                parameter = ((Map) parameter).get("list");
            }
            if (parameter instanceof Collection) {
                afterParameter = new ArrayList<>((Collection<Object>) parameter);
            } else if (parameter != null) {
                afterParameter = Collections.singletonList(parameter);
            }
        }
        AuditCommand afterCommand = createAuditCommand(mappedStatement, AuditCommandType.AFTER);
        if (AuditCommandThreadLocal.get() != null) {
            AuditCommandThreadLocal.add(afterCommand);
        } else {
            afterCommand.execute();
        }
    }

    private AuditCommand createAuditCommand(MappedStatement mappedStatement, AuditCommandType auditCommandType) throws Exception {
        AuditCommand auditCommand = null;
        try {
            switch (mappedStatement.getSqlCommandType()) {
                case UPDATE:
                    if (auditCommandType.equals(AuditCommandType.BEFORE)) {
                        auditCommand = new BeforeUpdateCommand(true, true, null, MappedStatementBuilder.build(), ParameterBuilder.build());
                    }

                    if (auditCommandType.equals(AuditCommandType.AFTER)) {
                        auditCommand = new AfterCommand(true, true, null, MappedStatementBuilder.build(), ParameterBuilder.build());
                    }
                    break;
                case INSERT:
                    if (auditCommandType.equals(AuditCommandType.BEFORE)) {
                        auditCommand = new BeforeInsertCommand();
                    }
                    if (auditCommandType.equals(AuditCommandType.AFTER)) {
                        auditCommand = new AfterCommand(true, true, null, MappedStatementBuilder.build(), ParameterBuilder.build());
                    }
                    break;
                default:
                    break;
            }
        } catch (AuditObjectNotFoundException e) {
            log.debug("Not fount open auditobject object");
        }
        return auditCommand;
    }

}
