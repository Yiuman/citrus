package com.github.yiuman.citrus.security.authenticate;

import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.http.ResponseStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 认证相关的控制器
 *
 * @author yiuman
 * @date 2020/3/26
 */
@RestController
public class AuthenticateController {

    private final AuthenticateProcessor authenticateProcessor;

    public AuthenticateController(AuthenticateProcessor authenticateProcessor) {
        this.authenticateProcessor = authenticateProcessor;
    }

    @PostMapping("#{citrusProperties.security.authenticateEndpoint}")
    public ResponseEntity<JwtToken> authenticate(HttpServletRequest request) {
        //若是JSON请求，需要重新构造一个request
        return ResponseEntity.ok(new JwtToken(authenticateProcessor.token(request)));
    }

    @Data
    @AllArgsConstructor
    private static class JwtToken {

        String token;
    }

    @ControllerAdvice
    @Slf4j
    static class ExceptionAdvice {

        /**
         * 异常处理
         */

        @ExceptionHandler(value = AuthenticationException.class)
        @ResponseBody
        public ResponseEntity<Void> exceptionHandler(AuthenticationException e) {
            log.error(e.getMessage(),e);
            return ResponseEntity.error(ResponseStatusCode.UN_AUTHENTICATION, e.getMessage());
        }

    }
}
