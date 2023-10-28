package com.fantasque.auth.service.impl;

import com.fantasque.auth.service.SysUserService;
import com.fantasque.common.exception.MyException;
import com.fantasque.common.result.ResultCodeEnum;
import com.fantasque.model.system.SysUser;
import com.fantasque.security.custom.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final int Disable = 0;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        // 验证用户名 账号不存在
        if (null == sysUser) {
            throw new MyException(ResultCodeEnum.LOGIN_USER_ERROR);
        }
        // 验证状态
        if (Disable == sysUser.getStatus()) { // 为 0 被禁用
            throw new MyException(ResultCodeEnum.LOGIN_STATUS_ERROR);
        }
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
