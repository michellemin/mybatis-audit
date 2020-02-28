package com.michelle.builder;

import com.michelle.annotations.AuditObjectAnn;
import com.michelle.utils.AuditCommandType;
import com.michelle.utils.AuditObjectNotFoundException;
import com.michelle.utils.AuditRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.*;

import java.lang.reflect.Field;
import java.util.*;

public class MappedStatementBuilder {
    private MappedStatement mappedStatement;
    private SqlMeta sqlMeta;
    private String id;
    private SqlSource sqlSource;
    private AuditObjectAnn auditObjectAnn;

    public MappedStatementBuilder(MappedStatement mappedStatement, Object parameter) throws Exception {
        this.mappedStatement = mappedStatement;
        String mapperId = mappedStatement.getId();
        int methodNameIndex = mapperId.lastIndexOf(".");
        String mapperInterfaceName = mapperId.substring(0, methodNameIndex);
        String mapperMethodName = mapperId.substring(methodNameIndex - 1, mapperId.length());
        Class mapperInterface = Class.forName(mapperInterfaceName);
        //TODO method annotation
        AuditObjectAnn auditObjectAnn = (AuditObjectAnn) mapperInterface.getAnnotation(AuditObjectAnn.class);
        if (auditObjectAnn == null) {
            throw new AuditObjectNotFoundException("Audit object not found!");
        }
        Class auditObjectType = auditObjectAnn.type();
        this.auditObjectAnn = auditObjectAnn;
        Field[] fields = auditObjectType.getDeclaredFields();
        SqlParser sqlParser = new DefaultSqlParser(mappedStatement.getSqlCommandType());
        this.sqlMeta = sqlParser.parse(mappedStatement, mappedStatement.getBoundSql(parameter).getSql());
    }


    public MappedStatementBuilder buildSqlSource(SqlSource sqlSource, AuditCommandType auditCommandType) {
        if (sqlSource != null) {
            this.sqlSource = sqlSource;
        } else {
            SqlSourceFactory sqlSourceFactory = null;
            switch (auditCommandType) {
                case AFTER:
                    sqlSourceFactory = new AfterSqlSourceFactory(sqlMeta, mappedStatement);
                    break;
                case BEFORE:
                    sqlSourceFactory = new BeforeSqlSourceFactory(sqlMeta, mappedStatement);
            }
            if (sqlSourceFactory != null) {
                this.sqlSource = sqlSourceFactory.create();
            }
        }
        return this;
    }

    public MappedStatementBuilder buildId(String id) {
        if (StringUtils.isNotBlank(id)) {
            this.id = id;
        }
        this.id = this.mappedStatement.getId() + ".audit";
        if (StringUtils.isBlank(id)) {
            throw new AuditRuntimeException("Audit mapper id is null!");
        }
        return this;
    }

    public MappedStatement build() {
        MappedStatement target;
        if (this.mappedStatement.getConfiguration().hasStatement(id)) {
            target = this.mappedStatement.getConfiguration().getMappedStatement(id);
        } else {
            List<ResultMap> resultMapList = new ArrayList<>();
            ResultMap resultMap = new ResultMap.Builder(this.mappedStatement.getConfiguration(), id + "-Inline", auditObjectAnn.type(), new ArrayList<>())
                    .build();
            resultMapList.add(resultMap);
            target = new MappedStatement.Builder(this.mappedStatement.getConfiguration(), id, sqlSource, SqlCommandType.SELECT)
                    .resultMaps(resultMapList)
                    .build();
            this.mappedStatement.getConfiguration().addMappedStatement(target);
        }
        return target;
    }
}
