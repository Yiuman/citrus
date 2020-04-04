package com.github.yiuman.citrus;

import com.github.yiuman.citrus.starter.EnableCitrusAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yiuman
 * @date 2020/3/4
 */
@SpringBootApplication
@EnableCitrusAdmin
public class CitrusApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitrusApplication.class, args);
    }
}
