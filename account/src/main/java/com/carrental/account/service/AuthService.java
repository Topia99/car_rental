package com.carrental.account.service;

import com.carrental.account.doa.AccountRepository;
import com.carrental.account.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class AuthService {
    private final AuthenticationManager am;
    private final JwtService jwt;
    private final AccountRepository repo;

    public AuthService(AuthenticationManager am, JwtService jwt, AccountRepository repo){
        this.am = am;
        this.jwt = jwt;
        this.repo = repo;
    }

    public record TokenPair(JwtService.IssuedToken access, JwtService.IssuedToken refresh) {}

    public TokenPair login(String email, String password){
        // Build an "unauthenticated" token (this just holds credentials; not yet authenticated)
        Authentication auth = am.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        String principalEmail = ((UserDetails) auth.getPrincipal()).getUsername();
        Account user = repo.findByEmail(principalEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var access = jwt.generateAccessToken(user.getEmail(), user.getId(), Map.of("role", "USER"));
        var refresh = jwt.generateRefreshToken(user.getId());
        return new TokenPair(access, refresh);
    }

    public TokenPair refresh(String refreshJwt){
        var jws = jwt.parse(refreshJwt);
        var claims = jws.getBody();
        String typ = claims.get("typ", String.class);
        if(!"refresh".equals(typ)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token type");
        }

        long uid;
        try{
            uid = Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid subject");
        }

        Account user = repo.findById(uid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var access = jwt.generateAccessToken(user.getEmail(), user.getId(), Map.of("role", "USER"));
        var refresh = jwt.generateRefreshToken(user.getId());
        return new TokenPair(access, refresh);
    }
}
