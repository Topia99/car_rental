package com.carrental.account.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final Key key;
    private final long accessMinutes;
    private final long refreshDays;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.access-minutes}") long accessMinutes,
                      @Value("${app.jwt.refresh-days}") long refreshDays) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    public record IssuedToken(String token, long expiresAtEpochSeconds) {}

    public IssuedToken generateAccessToken(String email, long uid, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessMinutes * 60);
        String jwtStr = Jwts.builder()
                .setSubject(email)
                .addClaims(extraClaims)
                .claim("uid", uid)
                .claim("typ", "access")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new IssuedToken(jwtStr, exp.getEpochSecond());
    }

    public IssuedToken generateRefreshToken(Long uid){
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofDays(refreshDays));
        String jwtStr = Jwts.builder()
                .setSubject(Long.toString(uid))
                .claim("typ", "refresh")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new IssuedToken(jwtStr, exp.getEpochSecond());
    }

    public Jws<Claims> parse(String jwt){


        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        return parser.parseClaimsJws(jwt);
    }

}
