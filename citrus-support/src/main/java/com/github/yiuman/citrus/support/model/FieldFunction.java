package com.github.yiuman.citrus.support.model;

import java.util.function.Function;

/**
 * 字段处理执行器
 *
 * @param <T> 实体类型
 * @param <R> 返回类型
 * @author yiuman
 * @date 2020/5/11
 */
public class FieldFunction<T, R> {

    private String filedName;

    private Function<T, R> function;

    public FieldFunction() {
    }

    public FieldFunction(String filedName, Function<T, R> function) {
        this.filedName = filedName;
        this.function = function;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public Function<T, R> getFunction() {
        return function;
    }

    public void setFunction(Function<T, R> function) {
        this.function = function;
    }
}
