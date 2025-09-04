package com.carrental.security;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityCommonConfig {
    @Bean
    JwtService jwtService(JwtProperties props) {
        return new JwtService(props);
    }

    @Bean
    JwtAuthFilter jwtAuthFilter(JwtService jwt){
        return new JwtAuthFilter(jwt);
    }
}
