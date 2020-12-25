package com.github.yiuman.citrus.support.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 动态数据源配置
 *
 * @author yiuman
 * @date 2020/11/30
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DynamicDataSourceProperties {

    /**
     * 主数据源
     * 默认为spring.datasource
     */
    private String primary = "";

    /**
     * 是否严格模式，若为true找不到数据源抛出异常
     */
    private boolean strict = true;

    /**
     * 数据源名称与数据源配置
     */
    private Map<String, DataSourceProperties> multiples;

    private boolean enableMultipleTx = true;

    public DynamicDataSourceProperties() {
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public Map<String, DataSourceProperties> getMultiples() {
        return multiples;
    }

    public void setMultiples(Map<String, DataSourceProperties> multiples) {
        this.multiples = multiples;
    }

    public Map<String, DataSourceProperties> getDatasource() {
        return multiples;
    }

    public boolean isEnableMultipleTx() {
        return enableMultipleTx;
    }

    public void setEnableMultipleTx(boolean enableMultipleTx) {
        this.enableMultipleTx = enableMultipleTx;
    }
}
