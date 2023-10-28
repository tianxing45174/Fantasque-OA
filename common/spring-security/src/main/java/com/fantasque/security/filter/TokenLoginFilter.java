package com.fantasque.security.filter;

import com.fantasque.common.jwt.JwtHelper;
import com.fantasque.common.result.ResponseUtil;
import com.fantasque.common.result.Result;
import com.fantasque.common.result.ResultCodeEnum;
import com.fantasque.security.custom.CustomUser;
import com.fantasque.vo.system.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LaFantasque
 * @version 1.0
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * Filter配置
     * @param authenticationManager 用户认证管理器
     */
    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        // 指定登录接口及提交方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
    }

    /**
     * 登录认证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            // 使用 Jackson 的 ObjectMapper 从 HTTP 请求的输入流中读取 JSON 数据，并将其转换为 LoginVo 类的实例
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginVo.getUsername().trim(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return super.attemptAuthentication(request, response);
    }

    /**
     * 认证成功
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        // 获取用户信息
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        // 获取token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
//        super.successfulAuthentication(request, response, chain, authResult);
    }

    /**
     * 认证失败
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
//        System.out.println("++++++++++++++++++++");
//        System.out.println(failed);
//        System.out.println("++++++++++++++++++++");
        Result<Object> result = Result.fail();
        if (ResultCodeEnum.LOGIN_USER_ERROR.getMessage().equals(failed.getMessage())) {

            // 用户不存在
            ResponseUtil.out(response, result.resultCodeEnum(ResultCodeEnum.LOGIN_USER_ERROR));
        } else if ("Bad credentials".equals(failed.getMessage())) {
            // 用户密码错误
            ResponseUtil.out(response, result.resultCodeEnum(ResultCodeEnum.LOGIN_PASSWORD_ERROR));
        } else if (ResultCodeEnum.LOGIN_STATUS_ERROR.getMessage().equals(failed.getMessage())){
            // 用户已被禁用
            ResponseUtil.out(response, result.resultCodeEnum(ResultCodeEnum.LOGIN_STATUS_ERROR));
        } else {
            // 用户已被禁用
            ResponseUtil.out(response, result.resultCodeEnum(ResultCodeEnum.LOGIN_ERROR));
        }

//        super.unsuccessfulAuthentication(request, response, failed);
    }
}
