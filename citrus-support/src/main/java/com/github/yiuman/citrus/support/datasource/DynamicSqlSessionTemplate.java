package com.github.yiuman.citrus.support.datasource;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.ibatis.reflection.ExceptionUtil.unwrapThrowable;
import static org.mybatis.spring.SqlSessionUtils.*;

/**
 * 动态数据源SqlSessionTemplate，拷贝原SqlSessionTemplate
 *
 * @author yiuman
 * @date 2020/12/2
 */
public class DynamicSqlSessionTemplate extends SqlSessionTemplate {

    /**
     * 是否使用严格模式
     */
    private Boolean strict = false;

    private final SqlSessionFactory sqlSessionFactory;

    private final ExecutorType executorType;

    private final SqlSession sqlSessionProxy;

    private final PersistenceExceptionTranslator exceptionTranslator;

    private Map<Object, SqlSessionFactory> targetSqlSessionFactories;

    private SqlSessionFactory defaultTargetSqlSessionFactory;

    public DynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
    }

    public DynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        this(sqlSessionFactory, executorType, new MyBatisExceptionTranslator(
                sqlSessionFactory.getConfiguration().getEnvironment().getDataSource()
                , true
        ));
    }

    public DynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
                                     PersistenceExceptionTranslator exceptionTranslator) {

        super(sqlSessionFactory, executorType, exceptionTranslator);

        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;

        this.sqlSessionProxy = (SqlSession) newProxyInstance(
                SqlSessionFactory.class.getClassLoader(),
                new Class[]{SqlSession.class},
                new SqlSessionInterceptor());

        this.defaultTargetSqlSessionFactory = sqlSessionFactory;
    }

    public void setTargetSqlSessionFactories(Map<Object, SqlSessionFactory> targetSqlSessionFactories) {
        this.targetSqlSessionFactories = targetSqlSessionFactories;
    }

    public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {
        this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;
    }

    public void setStrict(Boolean strict) {
        this.strict = strict;
    }

    /***
     *  获取当前使用数据源对应的会话工厂
     */
    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        String dataSourceKey = DynamicDataSourceHolder.peek();
        SqlSessionFactory targetSqlSessionFactory = targetSqlSessionFactories.get(dataSourceKey);

        if (!StringUtils.isEmpty(dataSourceKey) && strict && targetSqlSessionFactory == null) {
            throw new IllegalArgumentException(String.format("can not find DataSource %s,please check your datasource settings", dataSourceKey));
        }

        if (targetSqlSessionFactory != null) {
            return targetSqlSessionFactory;
        } else if (defaultTargetSqlSessionFactory != null) {
            return defaultTargetSqlSessionFactory;
        } else {
            Assert.notNull(targetSqlSessionFactories, "Property 'targetSqlSessionFactories' or 'defaultTargetSqlSessionFactory' are required");
            Assert.notNull(defaultTargetSqlSessionFactory, "Property 'defaultTargetSqlSessionFactory' or 'targetSqlSessionFactories' are required");
        }
        return this.sqlSessionFactory;
    }


    /**
     * 这个方法的实现和父类的实现是基本一致的，唯一不同的就是在getSqlSession方法传参中获取会话工厂的方式
     */
    private class SqlSessionInterceptor implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在getSqlSession传参时候，用我们重写的getSqlSessionFactory获取当前数据源对应的会话工厂
            final SqlSession sqlSession = getSqlSession(
                    DynamicSqlSessionTemplate.this.getSqlSessionFactory(),
                    DynamicSqlSessionTemplate.this.executorType,
                    DynamicSqlSessionTemplate.this.exceptionTranslator);
            try {
                Object result = method.invoke(sqlSession, args);
                if (!isSqlSessionTransactional(sqlSession, DynamicSqlSessionTemplate.this.getSqlSessionFactory())) {
                    sqlSession.commit(true);
                }
                return result;
            } catch (Throwable t) {
                Throwable unwrapped = unwrapThrowable(t);
                if (DynamicSqlSessionTemplate.this.exceptionTranslator != null && unwrapped instanceof PersistenceException) {
                    Throwable translated = DynamicSqlSessionTemplate.this.exceptionTranslator
                            .translateExceptionIfPossible((PersistenceException) unwrapped);
                    if (translated != null) {
                        unwrapped = translated;
                    }
                }
                throw unwrapped;
            } finally {
                closeSqlSession(sqlSession, DynamicSqlSessionTemplate.this.getSqlSessionFactory());
            }
        }
    }

    @Override
    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    @Override
    public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
        return this.exceptionTranslator;
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.sqlSessionProxy.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return this.sqlSessionProxy.selectOne(statement, parameter);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return this.sqlSessionProxy.selectMap(statement, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return this.sqlSessionProxy.selectMap(statement, parameter, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement) {
        return this.sqlSessionProxy.selectCursor(statement);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter) {
        return this.sqlSessionProxy.selectCursor(statement, parameter);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return this.sqlSessionProxy.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return this.sqlSessionProxy.selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectList(statement, parameter, rowBounds);
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, handler);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, parameter, handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    @Override
    public int insert(String statement) {
        return this.sqlSessionProxy.insert(statement);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return this.sqlSessionProxy.insert(statement, parameter);
    }

    @Override
    public int update(String statement) {
        return this.sqlSessionProxy.update(statement);
    }

    @Override
    public int update(String statement, Object parameter) {
        return this.sqlSessionProxy.update(statement, parameter);
    }

    @Override
    public int delete(String statement) {
        return this.sqlSessionProxy.delete(statement);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return this.sqlSessionProxy.delete(statement, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return getConfiguration().getMapper(type, this);
    }

    @Override
    public void clearCache() {
        this.sqlSessionProxy.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        Configuration configuration = sqlSessionFactory.getConfiguration();
        //Spring初始化Bean只会给默认的一个Configuration进行Mapper扫描及注入
        // 这里如果调用了其他的数据源，是没有初始化MappedStatement的，需要从默认的进行初始化后再调用
        //不然会出现 Invalid bound statement (not found) 异常
        if (sqlSessionFactory != defaultTargetSqlSessionFactory && CollectionUtils.isEmpty(configuration.getMappedStatements())) {
            //可能会有缓存先清掉
            GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
            globalConfig.getMapperRegistryCache().clear();
            defaultTargetSqlSessionFactory.getConfiguration().getMapperRegistry()
                    .getMappers().forEach(configuration::addMapper);
        }
        return configuration;
    }

    @Override
    public Connection getConnection() {
        return this.sqlSessionProxy.getConnection();
    }

    @Override
    public List<BatchResult> flushStatements() {
        return this.sqlSessionProxy.flushStatements();
    }

}
