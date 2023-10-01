package com.fantasque.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LaFantasque
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan("com.fantasque") // 指定扫描包
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);
    }
}
