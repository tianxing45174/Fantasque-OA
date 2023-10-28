package com.fantasque.security.custom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return new BCryptPasswordEncoder().encode(charSequence.toString());
    }

    /**
     *
     * @param charSequence 需要验证的密码
     * @param s 用户正确密码
     * @return true:正确 false:错误
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return new BCryptPasswordEncoder().matches(charSequence, s);
    }
}
