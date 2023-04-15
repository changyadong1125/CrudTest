package com.atguigu.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * project:CrudTest
 * package:com.atguigu.dao
 * class:BaseDao
 *
 * @author: smile
 * @create: 2023/3/21-16:50
 * @Version: v1.0
 * @Description:
 */
public class BaseDao<T> {
    private static final ThreadLocal<Connection> THREAD_LOCAL = new ThreadLocal<>();
    private static final DataSource DATA_SOURCE;
    private final Class<?> clazz;
    private final QueryRunner queryRunner = new QueryRunner();

    static {
        try {
            Properties properties = new Properties();
            properties.load(BaseDao.class.getClassLoader().getResourceAsStream("db.properties"));
            DATA_SOURCE = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BaseDao() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType superclass = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        try {
            clazz = Class.forName(actualTypeArguments[0].getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        Connection connection = THREAD_LOCAL.get();
        if (connection == null) {
            try {
                connection = DATA_SOURCE.getConnection();
                THREAD_LOCAL.set(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public void close() {
        Connection connection = THREAD_LOCAL.get();
        if (connection != null) {
            try {
                connection.close();
                THREAD_LOCAL.remove();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                close();
            }
        }
    }

    @SuppressWarnings("all")
    public List<T> getList(String sql, Object... params) {
        Connection connection = getConnection();
        try {
            List<?> query = queryRunner.query(connection, sql, new BeanListHandler<>(clazz), params);
            return (List<T>) query;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    @SuppressWarnings("all")
    public T getBean(String sql, Object... params) {
        Connection connection = getConnection();
        try {
            Object query = queryRunner.query(connection, sql, new BeanHandler<>(clazz), params);
            return (T) query;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public Object getObject(String sql, Object... params) {
        Connection connection = getConnection();
        try {
            return queryRunner.query(connection, sql, new ScalarHandler(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public boolean update(String sql, Object... param) {
        Connection connection = getConnection();
        try {
            return queryRunner.update(connection, sql, param) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }

    }
}
