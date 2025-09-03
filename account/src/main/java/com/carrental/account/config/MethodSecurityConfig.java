package com.carrental.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // Spring Security 6 / Boot 3
class MethodSecurityConfig {}
