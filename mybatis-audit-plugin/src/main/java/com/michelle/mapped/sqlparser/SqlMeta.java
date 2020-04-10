package com.michelle.mapped.sqlparser;

import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.SqlNode;

import java.util.List;

public class SqlMeta {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 临时表名
     */
    private String tempTableName;
    /**
     * where子句
     */
    private String whereClause;
    /**
     * sql类型
     */
    private SqlCommandType sqlCommandType;


    private List<SqlNode> sqlNodes;
    private String columns;
    List<ParameterMapping> parameterMappingList;

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTempTableName() {
        return tempTableName;
    }

    public void setTempTableName(String tempTableName) {
        this.tempTableName = tempTableName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public List<SqlNode> getSqlNodes() {
        return sqlNodes;
    }

    public void setSqlNodes(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
