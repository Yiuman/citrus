package com.github.yiuman.citrus.support.widget;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author yiuman
 * @date 2021/12/31
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class Button extends BaseWidget<Button, String> {

    /**
     * 按钮集合，如不为空，则为按钮组
     */
    private List<Button> actions;

    /**
     * 是否脚本，若为脚本，action将动态构建执行
     */
    private boolean script = false;

    public Button() {
    }

    @Override
    public String getWidgetName() {
        return "button";
    }

    public boolean isGroup() {
        return CollUtil.isNotEmpty(actions);
    }
}
