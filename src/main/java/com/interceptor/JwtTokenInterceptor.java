package com.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.model.enums.UserRoleEnum;
import com.utils.JwtUtils;
import com.utils.constants.JwtConstants;
import com.utils.enums.ErrorInfoEnum;
import com.utils.exception.BlogException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String token = req.getHeader(JwtConstants.TOKEN_HEADER);
        // 判断 token 是否非空，判断 token 的前缀
        if (Objects.isNull(token) || !token.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new BlogException(ErrorInfoEnum.NOT_LOGIN);
        }
        // 判断token是否过期
        token = token.replace(JwtConstants.TOKEN_PREFIX, StrUtil.EMPTY);
        if (JwtUtils.isTokenExpired(token)) {
            throw new BlogException(ErrorInfoEnum.TOKEN_EXPIRED);
        }
        // 判断 token 是否失效
        Boolean isMember = redisTemplate.opsForSet().isMember(JwtConstants.REDIS_KEY, token);
        if (isMember) {
            throw new BlogException(ErrorInfoEnum.TOKEN_INVALID);
        }
        Claims claims = JwtUtils.getTokenBody(token);
        String[] roles = Optional.ofNullable(claims.get(JwtConstants.ROLE_CLAIMS))
                .map(r -> r.toString().split(","))
                .orElse(new String[0]);
        // 判断角色是否正确
        if (!ArrayUtil.contains(roles, UserRoleEnum.ADMIN.getValue())) {
            throw new BlogException(ErrorInfoEnum.NO_AUTHORITY);
        }
        req.setAttribute("token", token);
        return true;
    }

   /* //1、token 的前缀是否正确。
    //2、token 是否过期。
    //3、token 中的角色是否包含 ADMIN
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String token = req.getHeader(JwtConstants.TOKEN_HEADER);
        if (token == null || !token.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new BlogException(ErrorInfoEnum.NOT_LOGIN);
        }
        token = token.replace(JwtConstants.TOKEN_PREFIX, StrUtil.EMPTY);
        if (JwtUtils.isTokenExpired(token)) {
            throw new BlogException(ErrorInfoEnum.TOKEN_EXPIRED);
        }
        Claims claims = JwtUtils.getTokenBody(token);
        String[] roles = Optional.ofNullable(claims.get(JwtConstants.ROLE_CLAIMS))
                .map(r -> r.toString().split(","))
                .orElse(new String[0]);
        if (!ArrayUtil.contains(roles, UserRoleEnum.ADMIN.getValue())) {
            throw new BlogException(ErrorInfoEnum.NO_AUTHORITY);
        }
        return true;
    }*/

}