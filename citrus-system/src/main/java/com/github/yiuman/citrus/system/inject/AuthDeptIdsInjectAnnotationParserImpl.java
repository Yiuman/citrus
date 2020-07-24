package com.github.yiuman.citrus.system.inject;

import com.github.yiuman.citrus.support.inject.InjectAnnotationParser;
import com.github.yiuman.citrus.system.service.DataRangeService;
import org.springframework.stereotype.Component;

/**
 * 权限部门的数据范围的注解注入解析器实现
 *
 * @author yiuman
 * @date 2020/7/23
 */
@Component
public class AuthDeptIdsInjectAnnotationParserImpl implements InjectAnnotationParser<AuthDeptIds> {

    private final DataRangeService dataRangeService;

    public AuthDeptIdsInjectAnnotationParserImpl(DataRangeService dataRangeService) {
        this.dataRangeService = dataRangeService;
    }

    @Override
    public Object parse(AuthDeptIds annotation) {
        return dataRangeService.getDeptIds();
    }
}
