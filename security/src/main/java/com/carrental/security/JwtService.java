package com.carrental.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

class JwtService {
    private final SecretKey key;
    private final String issuer; // may be null

    JwtService(JwtProperties props) {
        byte[] secret = Base64.getDecoder().decode(props.getSecretBase64());
        this.key = new SecretKeySpec(secret, "HmacSHA256");
        this.issuer = props.getIssuer();
    }

    Jws<Claims> parse(String jwt) {
        var builder = Jwts.parserBuilder().setSigningKey(key);
        if (issuer != null && !issuer.isBlank()) builder.requireIssuer(issuer);
        return builder.build().parseClaimsJws(jwt); // throws if invalid/expired
    }
}
