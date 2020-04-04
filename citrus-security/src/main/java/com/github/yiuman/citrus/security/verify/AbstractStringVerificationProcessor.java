package com.github.yiuman.citrus.security.verify;

import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * 字符串类型验证类型抽象处理器，字符串验证类公用的验证逻辑实现
 *
 * @author yiuman
 * @date 2020/3/22
 */
public abstract class AbstractStringVerificationProcessor<T extends AbstractStringVerification> implements VerificationProcessor<T> {

    protected final VerificationRepository verificationRepository;

    protected final VerifyProperties verifyProperties;

    public AbstractStringVerificationProcessor(VerificationRepository verificationRepository, VerifyProperties verifyProperties) {
        this.verificationRepository = verificationRepository;
        this.verifyProperties = verifyProperties;
    }

    @Override
    public void send(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        T generate = generate(httpServletRequest);
        response.getWriter().write(generate.getValue());
    }

    @Override
    public void validate(HttpServletRequest request) throws VerificationException {
        //获取当前请求的验证信息参数
        String verificationType = verificationType();
        String verificationParameter = request.getParameter(verificationType);

        Verification<?> verification = verificationRepository.find(request);
        if (Strings.isBlank(verificationParameter)) {
            throw new VerificationException(String.format("%s验证码的值不能为空", verificationType));
        }

        if (LocalDateTime.now().isAfter(verification.validTimeInSeconds())) {
            throw new VerificationException("验证码超时");
        }

        Object value = verification.getValue();
        if (value instanceof String) {
            value =  ((String) value).toLowerCase();
        }

        if (!value.equals(verificationParameter)){
            throw new VerificationException("验证码错误");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String verificationType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            //参数化类型
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            //返回表示此类型实际类型参数的 Type 对象的数组
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return ((Class<T>) actualTypeArguments[0]).getSimpleName().toLowerCase();
        }

        return null;
    }
}
