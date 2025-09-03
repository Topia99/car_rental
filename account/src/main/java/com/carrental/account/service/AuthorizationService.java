package com.carrental.account.service;

import com.carrental.account.config.AccountPrincipal;
import com.carrental.account.doa.AccountRepository;
import com.carrental.account.doa.HostProfileRepository;
import com.carrental.account.model.HostStatus;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("authz")
public class AuthorizationService {
    private final HostProfileRepository hostRepo;

    public AuthorizationService(HostProfileRepository hostRepo){
        this.hostRepo = hostRepo;
    }


    public boolean canAccessAccount(Long targetId, Authentication auth){
        if(auth == null || !auth.isAuthenticated()) return false;
        Object details = auth.getDetails();
        if(details instanceof Claims claims){
            Object uid = claims.get("uid");
            if(uid instanceof Number n){
                return targetId.equals(n.longValue());
            }
        }
        return false;
    }



    public boolean hostIsVerified(Authentication auth){
        if(hasRole(auth, "ROLE_ADMIN")) return true;
        Long uid = currentUserId(auth).orElse(null);
        //Get Account from db
        return hostRepo.findByUser_id(uid)
                .map(h -> h.getStatus() == HostStatus.VERIFIED)
                .orElse(false);
    }

    // Helper method
    private boolean hasRole(Authentication auth, String role){
        if(auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private Optional<Long> currentUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        Object p = auth.getPrincipal();
        if (p instanceof AccountPrincipal ap) return Optional.of(ap.getId());
//        // if using JWT auth in some flows:
//        if (auth instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken jwt) {
//            Object uid = jwt.getToken().getClaim("uid");
//            if (uid instanceof Number n) return Optional.of(n.longValue());
//        }
        return Optional.empty();
    }

}
