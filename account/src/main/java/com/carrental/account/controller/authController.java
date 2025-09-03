package com.carrental.account.controller;

import com.carrental.account.dto.AccessTokenResponse;
import com.carrental.account.dto.LoginRequest;
import com.carrental.account.service.AuthService;
import com.carrental.account.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class authController {
    private final AuthService auth;

    public authController(AuthService auth){
        this.auth = auth;
    }




    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginRequest req, HttpServletResponse res){
        var pair = auth.login(req.email(), req.password());

        // Set/rotate refresh Cookie (HttpOnly)
        setRefreshCookie(res, pair.refresh());

        return ResponseEntity.ok(new AccessTokenResponse(pair.access().token(), pair.access().expiresAtEpochSeconds()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(
            @CookieValue(name="refresh_token", required = false) String refreshCookie,
            HttpServletResponse res){
        if(refreshCookie == null || refreshCookie.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var pair = auth.refresh(refreshCookie); // Verify & rotate
        setRefreshCookie(res, pair.refresh());

        return ResponseEntity.ok(new AccessTokenResponse(pair.access().token(), pair.access().expiresAtEpochSeconds()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res){
        deleteRefreshCookie(res);
        return ResponseEntity.noContent().build();
    }


    // --- cookie helpers ---
    private void setRefreshCookie(HttpServletResponse res, JwtService.IssuedToken refresh){
        long maxAge = Math.max(0, refresh.expiresAtEpochSeconds() - Instant.now().getEpochSecond());
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh.token())
                .httpOnly(true)         // JavaScript Cannot read the cookie, which protects the token from XSS theft.
                .secure(false)          // Set true in production (HTTPS)
                .sameSite("Strict")     // "Lax" need cross-site navigation
                .path("/")   // cookie goes only to refresh endpoint
                .maxAge(Duration.ofSeconds(maxAge))
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void deleteRefreshCookie(HttpServletResponse res){
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
