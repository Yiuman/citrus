package com.github.yiuman.citrus.security.verify;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Redis验证信息仓库
 *
 * @author yiuman
 * @date 2020/3/23
 */
public class RedisVerificationRepository implements VerificationRepository {

    private static final String VERIFICATION_PARAMETER = "verifyId:";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisVerificationRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(HttpServletRequest request, HttpServletResponse response, Verification<?> verification) {
        redisTemplate.opsForValue().set(getRedisKey(request), verification.getValue(), 60, TimeUnit.SECONDS);
    }

    @Override
    public Verification<?> find(HttpServletRequest request) {
        return (Verification<?>) redisTemplate.opsForValue().get(getRedisKey(request));
    }

    @Override
    public void remove(HttpServletRequest request) {
        redisTemplate.delete(getRedisKey(request));
    }

    private String getRedisKey(HttpServletRequest request) {
        String verificationKey = request.getParameter(VERIFICATION_PARAMETER);
        if (Strings.isBlank(verificationKey)) {
            throw new VerificationException("缺少verifyId");
        }

        return verificationKey;
    }
}
