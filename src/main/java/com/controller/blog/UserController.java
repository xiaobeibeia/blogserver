package com.controller.blog;

import cn.hutool.core.map.*;
import com.model.comm.Results;
import com.model.dto.UserDTO;
import com.service.UserService;
import com.utils.constants.JwtConstants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import javax.validation.*;
import java.util.*;

@Api("与用户相关的api接口")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Results<Map<String, Object>> login(@ApiParam(name = "用户登录信息", value = "传入json格式", required = true)
                                              @RequestBody @Valid UserDTO userDTO) {
        String token = userService.checkUsernamePassword(userDTO);
        return Results.ok("登录成功", MapUtil.of("token", token));
    }


  // 编写 logout 的api接口，因为 Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)，所以使用 set 存储 已经过期的 JWT
    @PostMapping("/auth/logout")
    @ApiOperation("用户注销登录")
    public Results<Object> logout(@RequestAttribute("token") String token) {
        System.out.println(token);
        System.out.println(JwtConstants.REDIS_KEY);

        redisTemplate.opsForSet().add(JwtConstants.REDIS_KEY, token);
        System.out.println(redisTemplate.opsForSet().pop(JwtConstants.REDIS_KEY));
        return Results.ok("退出登录成功", null);
    }
}
