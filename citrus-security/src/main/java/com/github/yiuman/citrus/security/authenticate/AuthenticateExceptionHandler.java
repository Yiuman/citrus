package com.github.yiuman.citrus.security.authenticate;

import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.http.ResponseStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 认真异常处理器
 *
 * @author yiuman
 * @date 2020/10/30
 */
@RestControllerAdvice
@Slf4j
public class AuthenticateExceptionHandler {

    public AuthenticateExceptionHandler() {
    }

    /**
     * Rest异常处理
     */
    @ExceptionHandler(value = {NoPermissionException.class})
    public ResponseEntity<Void> exceptionHandler(NoPermissionException e) {
        log.error(e.getMessage());
        return ResponseEntity.error(ResponseStatusCode.NO_PERMISSIONS, e.getMessage());
    }
}
