package com.michelle.mapped.sqlsource;

import org.apache.ibatis.mapping.SqlSource;

/**
 * @author michelle.min
 */
public interface SqlSourceFactory {
    SqlSource create();
}
