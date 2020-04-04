package com.github.yiuman.citrus.rbac.service;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * UserDetailsService实现，用于DaoAuthenticationProvider，适配retrieveUser逻辑
 *
 * @author yiuman
 * @date 2020/3/31
 * @see DaoAuthenticationProvider
 */
@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByLoginId(username)
                .map(user -> new User(user.getUsername(), user.getPassword(), null))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with login '%s'.", username)));
    }
}
