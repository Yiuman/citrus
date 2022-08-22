package com.github.yiuman.citrus.system;

import com.github.yiuman.citrus.support.model.BasePreOrderTree;
import com.github.yiuman.citrus.system.entity.Organization;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author yiuman
 * @date 2022/8/22
 */
@Slf4j
public class AssignableFromTest {

    @Test
    public void testIsAssignableFrom() {
        log.info(String.valueOf(Organization.class.isAssignableFrom(BasePreOrderTree.class)));
        log.info(String.valueOf(BasePreOrderTree.class.isAssignableFrom(Organization.class)));
    }
}
