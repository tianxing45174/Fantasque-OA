package com.fantasque.auth.controller;

import com.fantasque.auth.service.SysUserService;
import com.fantasque.common.exception.MyException;
import com.fantasque.common.jwt.JwtHelper;
import com.fantasque.common.result.Result;
import com.fantasque.common.result.ResultCodeEnum;
import com.fantasque.model.system.SysUser;
import com.fantasque.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        System.out.println("用户" + loginVo.getUsername() + "登录");
        SysUser user = sysUserService.getByUsername(loginVo.getUsername());
        // 用户验证
        // 验证用户名
        if (null == user) {
            throw new MyException(ResultCodeEnum.LOGIN_USER_ERROR);
        }
        // 验证状态
        if (0 == user.getStatus()) { // 为 0 被禁用
            throw new MyException(ResultCodeEnum.LOGIN_STATUS_ERROR);
        }
        // 验证密码
        if  (!new BCryptPasswordEncoder().encode(loginVo.getPassword()).equals(user.getPassword())) {
            throw new MyException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        HashMap<Object, Object> map = new HashMap<>();
        // 使用jwt获取token
        map.put("token", JwtHelper.createToken(user.getId(), user.getUsername()));
//        HashMap<Object, Object> map = new HashMap<>();
//        map.put("token","admin");
        return Result.ok(map);
    }

    /**
     * 登录成功 获取用户信息
     * @return
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        String username = JwtHelper.getUsername(request.getHeader("token"));
        Map<String, Object> map = sysUserService.getUserInfo(username);
//        Map<String, Object> map = new HashMap<>();
//        map.put("roles","[admin]");
//        map.put("name","admin");
//        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }

    /**
     * 退出登录
     * @return
     */
    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
