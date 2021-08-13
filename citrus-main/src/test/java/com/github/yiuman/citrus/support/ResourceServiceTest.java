package com.github.yiuman.citrus.support;

import com.github.yiuman.citrus.system.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author yiuman
 * @date 2021/8/13
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration
public class ResourceServiceTest {

    @Autowired
    private ResourceService resourceService;

    public ResourceServiceTest() {
    }

    @Test
    public void getByCode(){
        Assertions.assertNotNull(resourceService.getKeyColumn());;
    }
}
