package com.github.yiuman.citrus.support.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 检验工具
 *
 * @author yiuman
 * @date 2020/4/3
 */
public final class ValidateUtils {

    private ValidateUtils() {
    }

    /**
     * 验证器
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    /**
     * 校验实体，返回实体所有属性的校验结果
     */
    public static <T> ValidationResult validateEntity(T obj) {
        //解析校验结果
        Set<ConstraintViolation<T>> validateSet = validator.validate(obj, Default.class);
        return buildValidationResult(validateSet);
    }


    /**
     * 校验实体，返回实体所有属性的校验结果
     */
    public static <T> void defaultValidateEntity(T obj) {
        //解析校验结果
        validateEntityAndThrows(obj,result->new RuntimeException(result.getMessage()));
    }

    /**
     * 校验指定实体的指定属性是否存在异常
     */
    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        Set<ConstraintViolation<T>> validateSet = validator.validateProperty(obj, propertyName, Default.class);
        return buildValidationResult(validateSet);
    }

    public static <T, E extends Exception> void validateEntityAndThrows(T obj, Function<ValidationResult, E> func) throws E {
        ValidationResult validationResult = validateEntity(obj);
        if (validationResult.isHasErrors()) {
            throw func.apply(validationResult);
        }
    }

    /**
     * 将异常结果封装返回
     */
    private static <T> ValidationResult buildValidationResult(Set<ConstraintViolation<T>> validateSet) {
        ValidationResult validationResult = new ValidationResult();
        if (CollectionUtils.isNotEmpty(validateSet)) {
            validationResult.setHasErrors(true);
            Map<String, String> errorMsgMap = new HashMap<>();
            for (ConstraintViolation<T> constraintViolation : validateSet) {
                errorMsgMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            validationResult.setErrorMsg(errorMsgMap);
        }
        return validationResult;
    }

    public static class ValidationResult {
        /**
         * 是否有异常
         */
        private boolean hasErrors;

        /**
         * 异常消息记录
         */
        private Map<String, String> errorMsg;

        /**
         * 获取异常消息组装
         */
        public String getMessage() {
            if (errorMsg == null || errorMsg.isEmpty()) {
                return StringUtils.EMPTY;
            }
            StringBuilder message = new StringBuilder();
            errorMsg.forEach((key, value) -> {
                message.append(MessageFormat.format("{0}:{1} \r\n", key, value));
            });
            return message.toString();
        }


        public boolean isHasErrors() {
            return hasErrors;
        }

        public void setHasErrors(boolean hasErrors) {
            this.hasErrors = hasErrors;
        }

        public Map<String, String> getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(Map<String, String> errorMsg) {
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "ValidationResult{" +
                    "hasErrors=" + hasErrors +
                    ", errorMsg=" + errorMsg +
                    '}';
        }
    }
}
