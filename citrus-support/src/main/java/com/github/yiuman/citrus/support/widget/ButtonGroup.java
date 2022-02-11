package com.github.yiuman.citrus.support.widget;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 按钮组
 *
 * @author yiuman
 * @date 2022/1/20
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Data
public class ButtonGroup extends BaseWidget<ButtonGroup, List<Button>> {

    @Override
    public String getWidgetName() {
        return "button-group";
    }
}
