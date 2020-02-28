package com.michelle.builder;

import org.apache.ibatis.mapping.MappedStatement;

public interface SqlParser {
    SqlMeta parse(MappedStatement mappedStatement, Object parameter) throws Exception;
}
