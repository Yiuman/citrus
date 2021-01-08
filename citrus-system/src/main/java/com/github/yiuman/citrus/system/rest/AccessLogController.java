package com.github.yiuman.citrus.system.rest;

import cn.hutool.core.date.DateUtil;
import com.github.yiuman.citrus.support.crud.query.QueryParam;
import com.github.yiuman.citrus.support.crud.rest.BaseQueryController;
import com.github.yiuman.citrus.support.model.Header;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.system.entity.AccessLog;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 访问日志
 *
 * @author yiuman
 * @date 2020/9/24
 */
@RestController
@RequestMapping("/rest/access/log")
public class AccessLogController extends BaseQueryController<AccessLog, Long> {

    public AccessLogController() {
        addSortBy("createTime", true);
        setParamClass(AccessLogQuery.class);
    }

    @Data
    static class AccessLogQuery {

        @QueryParam
        private Long userId;

        @QueryParam(type = "in")
        private List<Long> resourceType;
    }

    @Override
    protected Page<AccessLog> createPage() throws Exception {
        Page<AccessLog> page = super.createPage();
        page.setHasSelect(false);
        page.addHeader("用户", "username").align(Header.Align.center);
        page.addHeader("IP地址", "ipAddress");
        page.addHeader("请求", "method_url", entity -> String.format("%s  %s", entity.getRequestMethod(), entity.getUrl()));
        page.addHeader("参数","params");
        page.addHeader("资源名称", "resourceName");
        page.addHeader("时间", "createTime_",entity-> DateUtil.format(entity.getCreateTime(),"yyyy-MM-dd hh:mm:ss"));
        return page;
    }

}
