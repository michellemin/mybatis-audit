package com.michelle.executor;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;

/**
 * @author michelle.min
 */
@Slf4j
public class MybatisExecutor implements AuditExecutor {
    private Executor delegate;
    private boolean autoClose;

    public MybatisExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    public MybatisExecutor(Executor delegate, boolean autoClose) {
        this.delegate = delegate;
        this.autoClose = autoClose;
    }

    @Override
    public <E> List<E> query(MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        List<E> result;
        try {
            result = delegate.query(mappedStatement, parameter, rowBounds, resultHandler);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return result;
    }

    @Override
    public int update(MappedStatement mappedStatement, Object parameter) throws SQLException {
        int result = 0;
        try {
            result = delegate.update(mappedStatement, parameter);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return result;
    }

    @Override
    public void close() {
        try {
            commit();
            delegate.getTransaction().close();
        } catch (SQLException e) {
            log.error("Close executor error", e);
        }
    }

    @Override
    public void commit() {
        try {
            delegate.commit(true);
        } catch (SQLException e) {
            log.error("Commit executor error", e);
        }
    }
}
