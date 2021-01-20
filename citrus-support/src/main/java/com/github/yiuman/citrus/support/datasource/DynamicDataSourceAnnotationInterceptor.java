package com.github.yiuman.citrus.support.datasource;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * DataSource注解的代理方法拦截器
 *
 * @author yiuman
 * @date 2020/11/30
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    private final DataSourceClassResolver dataSourceClassResolver;

    public DynamicDataSourceAnnotationInterceptor() {
        dataSourceClassResolver = new DataSourceClassResolver(true);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String dsKey = dataSourceClassResolver.findDSKey(invocation.getMethod(), invocation.getThis());
            if (StringUtils.isNotBlank(dsKey)) {
                DynamicDataSourceHolder.push(dsKey);
            }

            return invocation.proceed();
        } finally {
            DynamicDataSourceHolder.poll();
        }
    }

}