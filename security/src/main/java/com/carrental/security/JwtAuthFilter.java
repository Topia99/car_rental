package com.carrental.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwt;

    public JwtAuthFilter(JwtService jwt){
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String h = request.getHeader("Authorization");
        if(h != null && h.startsWith("Bearer ")){
            String jwtToken = h.substring(7);

            try{
                var jws = jwt.parse(jwtToken);
                Claims claims = jws.getBody();

                Long uid = null;
                Object u = claims.get("uid");
                if(u instanceof Number n){
                    uid = n.longValue();
                }

                String email = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);
            }


        }
    }
}
