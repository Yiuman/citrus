package com.github.yiuman.citrus.support.widget;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author yiuman
 * @date 2021/12/31
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
public class Button extends BaseWidget<Button, String> {

    /**
     * 是否脚本，若为脚本，action将动态构建执行
     */
    private boolean script = false;

    @Override
    public String getWidgetName() {
        return "button";
    }
}
