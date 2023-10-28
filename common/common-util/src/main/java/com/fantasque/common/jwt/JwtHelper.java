package com.fantasque.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author LaFantasque
 * @version 1.0
 */
public class JwtHelper {
    // token有效期
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    // 签名密钥
    private static String tokenSignKey = "123456";
    // 根据 用户id 和 用户名 生成token
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                // 分类
                .setSubject("AUTH-USER")
                // 设置token有效期
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                // 设置主体
                .claim("userId", userId)
                .claim("username", username)
                // 设置签名
                .signWith(SignatureAlgorithm.HS512, tokenSignKey) // 设置密钥
                .compressWith(CompressionCodecs.GZIP) // 设置压缩
                .compact();
        return token;
    }

    /**
     * 解析token 获取 用户id
     * @param token
     * @return
     */
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;
            // 根据密钥 解析token
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            // 获取主体
            Claims claims = claimsJws.getBody();
            // 获取用户id
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析token 获取 用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        String token = JwtHelper.createToken(1L, "admin");
//        System.out.println(token);
//        System.out.println(JwtHelper.getUserId(token));
//        System.out.println(JwtHelper.getUsername(token));
//    }
}