package com.fantasque.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author LaFantasque
 * @version 1.0
 */
public class main {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("test04"));
        System.out.println(new BCryptPasswordEncoder().encode("test"));
        System.out.println(new BCryptPasswordEncoder().encode("test02"));
        System.out.println(new BCryptPasswordEncoder().encode("test01"));
        System.out.println(new BCryptPasswordEncoder().encode("test03"));
    }
}
