package com.github.yiuman.citrus.support.model;

import java.util.function.Function;

/**
 * 字段处理执行器
 *
 * @author yiuman
 * @date 2020/5/11
 */
public class FieldFunction<T> {

    private String filedName;

    private Function<T, Object> function;

    public FieldFunction() {
    }

    public FieldFunction(String filedName, Function<T, Object> function) {
        this.filedName = filedName;
        this.function = function;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public Function<T, Object> getFunction() {
        return function;
    }

    public void setFunction(Function<T, Object> function) {
        this.function = function;
    }
}
