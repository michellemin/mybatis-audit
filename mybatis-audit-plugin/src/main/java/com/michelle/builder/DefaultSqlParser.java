package com.michelle.builder;

import com.michelle.utils.AuditRuntimeException;
import com.michelle.utils.ReflectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author michelle.min
 */
public class DefaultSqlParser implements SqlParser {
    protected Pattern pattern;
    protected Map<String, Integer> groupIndexMap;
    private final static String UPDATE_REGEX = "(update|UPDATE)(\\s+)([a-zA-Z_0-9\\.]+)(\\s+[a-zA-Z_0-9]*){0,1}(\\s+)(set|SET)(.+)(where|WHERE)(\\s+)(.+)";
    private final static String INSERT_REGEX = "(INSERT|insert)(\\s+)(INTO|into)(\\s+)([a-zA-Z_0-9\\.]+)(\\s*)(.+)";

    public DefaultSqlParser(SqlCommandType sqlCommandType) {
        switch (sqlCommandType) {
            case UPDATE:
                this.pattern = Pattern.compile(UPDATE_REGEX);
                this.groupIndexMap = new HashMap<>(3);
                this.groupIndexMap.put("tableName", 3);
                this.groupIndexMap.put("tempTableName", 4);
                this.groupIndexMap.put("whereClause", 10);
                break;
            case INSERT:
                this.pattern = Pattern.compile(INSERT_REGEX);
                this.groupIndexMap = Collections.singletonMap("tableName", 5);
                break;
            default:
        }
    }

    public DefaultSqlParser(Pattern pattern, Map<String, Integer> groupIndexMap) {
        this.pattern = pattern;
        this.groupIndexMap = groupIndexMap;
    }

    private SqlMeta parseStaticSql(String sql) {
        SqlMeta metaObject = null;
        if (StringUtils.isNotBlank(sql)) {
            Matcher matcher = pattern.matcher(sql.replace("\n", " "));
            if (matcher.matches()) {
                metaObject = new SqlMeta();
                metaObject.setSqlCommandType(SqlCommandType.UPDATE);
                String tableName;
                if (StringUtils.isBlank(tableName = StringUtils.trimToEmpty(matcher.group(groupIndexMap.get("tableName"))))) {
                    throw new AuditRuntimeException("Can't parse audit sql table name,input sql=" + sql);
                }
                metaObject.setTableName(tableName);
                Integer index;
                //Default value is empty
                metaObject.setTempTableName(StringUtils.EMPTY);
                if ((index = groupIndexMap.get("tempTableName")) != null) {
                    metaObject.setTempTableName(StringUtils.trimToEmpty(matcher.group(index)));
                }
                //Default value is empty
                metaObject.setWhereClause(StringUtils.EMPTY);
                if ((index = groupIndexMap.get("whereClause")) != null) {
                    metaObject.setWhereClause(StringUtils.trimToEmpty(matcher.group(index)));
                }
            } else {
                throw new AuditRuntimeException("Can't parse audit sql,input sql=" + sql);
            }
        }
        return metaObject;
    }

    @Override
    public SqlMeta parse(MappedStatement mappedStatement, Object parameter) throws Exception {
        String sql = mappedStatement.getBoundSql(parameter).getSql();
        SqlSource sqlSource = mappedStatement.getSqlSource();
        SqlMeta metaObject = parseStaticSql(sql);
        if (sqlSource instanceof DynamicSqlSource) {
            List<SqlNode> whereSqlNodeList = new ArrayList<>();
            DynamicSqlSource dynamicSqlSource = (DynamicSqlSource) sqlSource;
            MixedSqlNode sqlNode = ReflectionUtil.getFieldValue("rootSqlNode", dynamicSqlSource, MixedSqlNode.class);
            if (sqlNode != null) {
                String originalWhereClause = null;
                Object contentsValue = ReflectionUtil.getFieldValue("contents", sqlNode);
                List<SqlNode> sqlNodeList;
                if (contentsValue != null && CollectionUtils.isNotEmpty(sqlNodeList = (List<SqlNode>) contentsValue)) {
                    for (SqlNode s : sqlNodeList) {
                        if (StringUtils.isNotBlank(originalWhereClause)) {
                            whereSqlNodeList.add(s);
                        } else if (s instanceof StaticTextSqlNode) {
                            String text = ReflectionUtil.getFieldString("text", s);
                            int whereIndex;
                            if (StringUtils.isNotBlank(text) && (whereIndex = text.toUpperCase().lastIndexOf("WHERE")) != -1) {
                                originalWhereClause = text.substring(whereIndex, text.length());
                            }
                        }
                    }
                }
                if (StringUtils.isBlank(originalWhereClause)) {
                    throw new AuditRuntimeException("Audit parse sql error,sql={}" + sql);
                }
                metaObject.setSqlNodes(whereSqlNodeList);
            }
        }
        return metaObject;
    }
}
