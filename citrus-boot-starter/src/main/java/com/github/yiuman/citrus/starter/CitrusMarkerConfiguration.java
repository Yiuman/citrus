package com.github.yiuman.citrus.starter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yiuman
 * @date 2020/8/12
 */
@Configuration
public class CitrusMarkerConfiguration {

    @Bean
    public CitrusMarkerConfiguration.Marker marker() {
        return new Marker();
    }

    static class Marker {
    }
}
