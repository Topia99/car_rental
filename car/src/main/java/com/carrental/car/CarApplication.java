package com.carrental.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.carrental",
        exclude = org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class)
public class CarApplication {
    public static void main(String[] args){
        SpringApplication.run(CarApplication.class, args);
    }
}
