package com.michelle.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AfterSqlSourceFactory implements SqlSourceFactory{
    private SqlMeta sqlMeta;
    private MappedStatement mappedStatement;

    public AfterSqlSourceFactory(SqlMeta sqlMeta, MappedStatement mappedStatement) {
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

        sqlSb.append(" where ");
        if (StringUtils.isNotBlank(sqlMeta.getTempTableName())) {
            sqlSb.append(sqlMeta.getTempTableName());
            sqlSb.append(".");
        }
        //修改后结果查询sql使用id in条件
        sqlSb.append("id in ");
        List<SqlNode> sqlNodeList = new ArrayList<>();
        sqlNodeList.add(new StaticTextSqlNode(sqlSb.toString()));
        sqlNodeList.add(new ForEachSqlNode(this.mappedStatement.getConfiguration(),
                new MixedSqlNode(Collections.singletonList(new StaticTextSqlNode(" #{item} "))),
                "list", "index", "item", "(", ")", ","));
        return new DynamicSqlSource(this.mappedStatement.getConfiguration(), new MixedSqlNode(sqlNodeList));
    }
}
