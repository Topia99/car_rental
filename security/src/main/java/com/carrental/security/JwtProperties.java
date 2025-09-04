package com.carrental.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String secret;
//    private String issuer; // optional

    public String getSecret() { return secret; }
    public void setSecret(String s) { this.secret = s; }

//    public String getIssuer() { return issuer; }
//    public void setIssuer(String issuer) { this.issuer = issuer; }
}

