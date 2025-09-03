package com.carrental.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String secretBase64;
    private String issuer; // optional

    public String getSecretBase64() { return secretBase64; }
    public void setSecretBase64(String s) { this.secretBase64 = s; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
}

