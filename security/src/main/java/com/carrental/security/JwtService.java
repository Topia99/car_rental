package com.carrental.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

class JwtService {
    private final SecretKey key;
//    private final String issuer; // may be null

    JwtService(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.getSecret()));
//        this.issuer = props.getIssuer();
    }

    Jws<Claims> parse(String jwt) {
        var builder = Jwts.parserBuilder().setSigningKey(key);
        return builder.build().parseClaimsJws(jwt); // throws if invalid/expired
    }
}
