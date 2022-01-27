package com.github.yiuman.citrus.mda.ddl;

/**
 * 元数据执行操作的时间
 *
 * @author yiuman
 * @date 2021/4/26
 */
public enum Action {

    /**
     * 创建事件
     */
    CREATE("create", "创建"),

    UPDATE("update", "更新"),

    DELETE("delete", "删除");

    private final String actionCode;

    private final String actionName;

    Action(String actionCode, String actionName) {
        this.actionCode = actionCode;
        this.actionName = actionName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public String getActionName() {
        return actionName;
    }

}
