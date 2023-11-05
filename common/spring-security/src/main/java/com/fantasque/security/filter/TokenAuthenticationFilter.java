package com.fantasque.security.filter;

import com.alibaba.fastjson2.JSON;
import com.fantasque.common.jwt.JwtHelper;
import com.fantasque.common.result.ResponseUtil;
import com.fantasque.common.result.Result;
import com.fantasque.common.result.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LaFantasque
 * @version 1.0
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;
    // 登录超时标识
    private static final SimpleGrantedAuthority LOGIN_TIME_OUT = new SimpleGrantedAuthority("nullAuth");

    public TokenAuthenticationFilter() {

    }
    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("uri:" + request.getRequestURI());
        // 放行登录接口
        if ("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        if (null == authentication) { // 未登录
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_NULL_ERROR));
        } else if (authentication.getAuthorities().contains(LOGIN_TIME_OUT)) { // 登录数据过期
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_TIME_OUT_ERROR).message("登录超时，请刷新页面重新登录！"));
        } else { // 登录成功
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // header中获取token 判断是否登录
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            // 登录失败
            if (StringUtils.isEmpty(username)) {
                return null;
            }
            logger.info("username:"+username);
            // redis 中获取 权限数据
            String authString = (String) redisTemplate.opsForValue().get(username);
            // 封装权限数据
            List<Map> mapList = JSON.parseArray(authString, Map.class);
            ArrayList<SimpleGrantedAuthority> authorityArrayList = new ArrayList<>();
            // 数据以过期
            if (StringUtils.isEmpty(authString)) {
                authorityArrayList.add(LOGIN_TIME_OUT);
                return new UsernamePasswordAuthenticationToken(username, null, authorityArrayList);
            }
            for (Map map : mapList) {
                authorityArrayList.add(new SimpleGrantedAuthority((String) map.get("authority")));
            }
            return new UsernamePasswordAuthenticationToken(username, null, authorityArrayList);
        }
        // 未登录
        return null;
    }
}