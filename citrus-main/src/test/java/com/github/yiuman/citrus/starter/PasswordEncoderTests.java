package com.github.yiuman.citrus.starter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author yiuman
 * @date 2020/4/2
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PasswordEncoderTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        System.out.println(passwordEncoder.encode("123456"));
    }

}
