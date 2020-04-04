package com.github.yiuman.citrus.starter;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author yiuman
 * @date 2020/4/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PasswordEncoderTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        System.out.println(passwordEncoder.encode("123456"));

    }

}
