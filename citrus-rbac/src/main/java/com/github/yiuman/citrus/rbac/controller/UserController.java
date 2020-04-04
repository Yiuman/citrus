package com.github.yiuman.citrus.rbac.controller;

import com.github.yiuman.citrus.rbac.dto.UserDto;
import com.github.yiuman.citrus.rbac.service.HasLoginHook;
import com.github.yiuman.citrus.rbac.service.UserService;
import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制层
 *
 * @author yiuman
 * @date 2020/3/30
 */
@RestController
@RequestMapping("/rest/users")
@Authorize(HasLoginHook.class)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.selectAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping
    public ResponseEntity<Long> saveUser(UserDto user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping
    public ResponseEntity<Long> update(UserDto user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.removeById(userId));
    }

}
