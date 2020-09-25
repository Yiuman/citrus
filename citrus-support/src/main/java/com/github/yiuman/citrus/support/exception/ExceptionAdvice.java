package com.github.yiuman.citrus.support.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.http.ResponseStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常处理
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Rest异常处理
     */

    @ExceptionHandler(value = {RestException.class, ValidateException.class})
    @ResponseBody
    public ResponseEntity<Void> exceptionHandler(RestException e) {
        log.error(e.getMessage());
        return ResponseEntity.error(e.getCode(), e.getMessage());
    }

    /**
     * 数据校验异常处理
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public ResponseEntity<Void> exceptionHandler(BindException e) throws JsonProcessingException {
        log.error(e.getMessage());
        return ResponseEntity.error(ResponseStatusCode.BAD_REQUEST, OBJECT_MAPPER.writeValueAsString(getErrorResults(e.getFieldErrors())));
    }

    /**
     * 数据校验异常处理
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<Void> exceptionHandler(MethodArgumentNotValidException e) throws JsonProcessingException {
        log.error(e.getMessage());
        return ResponseEntity.error(ResponseStatusCode.BAD_REQUEST, OBJECT_MAPPER.writeValueAsString(getErrorResults(e.getBindingResult().getFieldErrors())));
    }

    private List<ErrorResult> getErrorResults(List<FieldError> fieldErrors) {
        return fieldErrors.parallelStream()
                .map(error -> new ErrorResult(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private static class ErrorResult {

        private String field;

        private String message;

        public ErrorResult(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ErrorResult{" +
                    "field='" + field + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    /**
     * 异常处理
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public ResponseEntity<Void> exceptionHandler(Throwable throwable) {
        log.error("未知异常", throwable);
        return ResponseEntity.error(ResponseStatusCode.SERVER_ERROR, "未知异常");
    }

}
