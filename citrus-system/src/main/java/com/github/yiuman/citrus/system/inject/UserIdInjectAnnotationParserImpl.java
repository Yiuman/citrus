package com.github.yiuman.citrus.system.inject;

import com.github.yiuman.citrus.support.inject.InjectAnnotationParser;
import org.springframework.stereotype.Component;

/**
 * UserId用户Id注入解析器实现
 *
 * @author yiuman
 * @date 2020/7/23
 */
@Component
public class UserIdInjectAnnotationParserImpl implements InjectAnnotationParser<UserId> {

    public UserIdInjectAnnotationParserImpl() {
    }

    @Override
    public Object parse(UserId annotation) {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
