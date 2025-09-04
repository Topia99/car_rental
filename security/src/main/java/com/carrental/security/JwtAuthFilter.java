package com.carrental.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

                @SuppressWarnings("unchecked")
                var authorities = ((List<String>) claims.getOrDefault("roles", List.of()))
                        .stream().map(r -> r.startsWith("ROLE_") ? r : "ROLE_"+r)
                        .map(SimpleGrantedAuthority::new).toList();

                var principal = new AccountPrincipal(uid, email, authorities);

                var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e){

            }
        }
        filterChain.doFilter(request, response);
    }
}
