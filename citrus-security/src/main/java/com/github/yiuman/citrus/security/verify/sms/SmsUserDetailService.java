package com.github.yiuman.citrus.security.verify.sms;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public interface SmsUserDetailService {

    UserDetails loadUserByMobile(String mobile) throws MobileNotFoundException;
}
