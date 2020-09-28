package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.system.dto.AccessLogQuery;
import com.github.yiuman.citrus.system.entity.AccessLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问日志
 *
 * @author yiuman
 * @date 2020/9/24
 */
@RestController
@RequestMapping("/rest/access/log")
public class AccessLogController extends BaseCrudController<AccessLog, Long> {

    public AccessLogController() {
        addSortBy("createTime",true);
        setParamClass(AccessLogQuery.class);
    }

}
