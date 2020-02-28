package com.michelle.executor;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;

public interface AuditExecutor {
    <E> List<E> query(MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

    int update(MappedStatement mappedStatement, Object parameter) throws SQLException;

    void close();

    void commit();
}
