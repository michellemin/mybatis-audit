package com.michelle.mapped.sqlsource;

import com.michelle.mapped.sqlparser.SqlMeta;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author michelle.min
 */
public class BeforeSqlSourceFactory implements SqlSourceFactory {
    private SqlMeta sqlMeta;
    private MappedStatement mappedStatement;

    public BeforeSqlSourceFactory(SqlMeta sqlMeta, MappedStatement mappedStatement) {
        this.sqlMeta = sqlMeta;
        this.mappedStatement = mappedStatement;
    }

    public SqlSource create() {
        StringBuilder sqlSb = new StringBuilder("select ");
        if (StringUtils.isNotBlank(sqlMeta.getTempTableName())) {
            for (String col : sqlMeta.getColumns().split(",")) {
                sqlSb.append(sqlMeta.getTempTableName()).append(".").append(col);
            }
        }
        SqlSource sqlSource;
        if (CollectionUtils.isNotEmpty(this.sqlMeta.getSqlNodes())) {
            List<SqlNode> sqlNodeList = new ArrayList<>();
            sqlNodeList.add(new StaticTextSqlNode(sqlSb.toString()));
            sqlNodeList.addAll(this.sqlMeta.getSqlNodes());
            sqlSource = new DynamicSqlSource(this.mappedStatement.getConfiguration(), new MixedSqlNode(sqlNodeList));
        } else {
            //根据where子句参数占位符获取参数mapping
            sqlSb.append(" where ");
            sqlSb.append(this.sqlMeta.getWhereClause());
            List<ParameterMapping> parameterMappingList = new ArrayList<>();
            List<ParameterMapping> auditParameterMappingList = this.sqlMeta.getParameterMappingList();
            String whereClause = this.sqlMeta.getWhereClause();
            int parameterCount = StringUtils.isNoneBlank(whereClause) ? (int) whereClause.chars().filter(ch -> ch == '?').count() : 0;
            if (parameterCount > 0 && auditParameterMappingList.size() > parameterCount) {
                parameterMappingList = auditParameterMappingList.subList(auditParameterMappingList.size() - parameterCount, auditParameterMappingList.size());
            }
            sqlSource = new StaticSqlSource(this.mappedStatement.getConfiguration(), sqlSb.toString(), parameterMappingList);
        }
        return sqlSource;
    }
}
