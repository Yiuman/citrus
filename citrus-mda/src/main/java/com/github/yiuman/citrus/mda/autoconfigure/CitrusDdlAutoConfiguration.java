package com.github.yiuman.citrus.mda.autoconfigure;


import com.github.yiuman.citrus.mda.ddl.DdlProcessor;
import com.github.yiuman.citrus.mda.ddl.DdlProcessorImpl;
import com.github.yiuman.citrus.mda.ddl.analyzer.ColumnMetadataAnalyzer;
import com.github.yiuman.citrus.mda.ddl.analyzer.IndexesMetadataAnalyzer;
import com.github.yiuman.citrus.mda.ddl.analyzer.TableMetadataAnalyzer;
import com.github.yiuman.citrus.mda.ddl.analyzer.impl.ColumnMetadataAnalyzerImpl;
import com.github.yiuman.citrus.mda.ddl.analyzer.impl.IndexesMetadataAnalyzerImpl;
import com.github.yiuman.citrus.mda.ddl.analyzer.impl.TableMetadataAnalyzerImpl;
import com.github.yiuman.citrus.mda.dml.DmlProcessor;
import com.github.yiuman.citrus.mda.dml.DmlProcessorImpl;
import com.github.yiuman.citrus.support.datasource.DynamicSqlSessionTemplate;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * DDL自动配置
 *
 * @author yiuman
 * @date 2021/4/20
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(name = {"com.github.yiuman.citrus.starter.CitrusAutoConfiguration"})
@ComponentScans({
        @ComponentScan("com.github.yiuman.citrus.mda")
})
@MapperScan(basePackages = "com.github.yiuman.citrus.ddl.mapper")
public class CitrusDdlAutoConfiguration {

    /**
     * 默认的DDL处理器
     *
     * @param dynamicSqlSessionTemplate mybatis动态数据源模板
     * @return DdlProcessor默认实现
     */
    @Bean
    @ConditionalOnMissingBean(DdlProcessor.class)
    public DdlProcessor ddlProcessor(DynamicSqlSessionTemplate dynamicSqlSessionTemplate) {
        return new DdlProcessorImpl(dynamicSqlSessionTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(DmlProcessor.class)
    public DmlProcessor dmlProcessor(DynamicSqlSessionTemplate dynamicSqlSessionTemplate) {
        return new DmlProcessorImpl(dynamicSqlSessionTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(TableMetadataAnalyzer.class)
    public TableMetadataAnalyzer tableMetadataAnalyzer() {
        return new TableMetadataAnalyzerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ColumnMetadataAnalyzer.class)
    public ColumnMetadataAnalyzer columnMetadataAnalyzer() {
        return new ColumnMetadataAnalyzerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(IndexesMetadataAnalyzer.class)
    public IndexesMetadataAnalyzer indexesMetadataAnalyzer() {
        return new IndexesMetadataAnalyzerImpl();
    }


}
