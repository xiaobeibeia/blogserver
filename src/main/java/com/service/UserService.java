package com.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.setting.Setting;
import com.model.dto.UserDTO;
import com.model.enums.UserRoleEnum;
import com.utils.JwtUtils;
import com.utils.enums.ErrorInfoEnum;
import com.utils.exception.BlogException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Objects;



@Service
public class UserService {
    @Autowired
    private Setting setting;

    /**
     * 校验用户名和密码
     *
     * @param userDTO 用户对象
     * @return 校验成功就返回token
     */
    public String checkUsernamePassword(UserDTO userDTO) {
        String username = setting.getStr("username");
        String password = setting.getStr("password");
        String Md5Password = MD5.create().digestHex(userDTO.getPassword());
        if (Objects.equals(username, userDTO.getUsername()) &&
                Objects.equals(password, Md5Password)) {
            return JwtUtils.createToken(username, CollUtil.newArrayList(UserRoleEnum.ADMIN.getValue()));
        } else {
            throw new BlogException(ErrorInfoEnum.USERNAME_PASSWORD_ERROR);
        }
    }
}
