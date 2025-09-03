package com.carrental.account.controller;

import com.carrental.account.config.AccountPrincipal;
import com.carrental.account.dto.HostProfileResponse;
import com.carrental.account.dto.HostProfileUpdateRequest;
import com.carrental.account.model.HostStatus;
import com.carrental.account.service.HostProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hosts")
public class HostController {
    private final HostProfileService service;

    public HostController(HostProfileService service) {
        this.service = service;
    }

    /** Step 1: user opts into hosting; adds HOST role + creates profile (PENDING) */
    @PostMapping("/me/enable")
    public HostProfileResponse enable(@AuthenticationPrincipal AccountPrincipal me){
        return service.enableHosting(me.getId());
    }

    @GetMapping("/me")
    public HostProfileResponse getHostProfile(@AuthenticationPrincipal AccountPrincipal me){
        return service.getForUser(me.getId());
    }

    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PutMapping("/me")
    public HostProfileResponse updateHostProfile(@AuthenticationPrincipal AccountPrincipal me,
                                                 @RequestBody HostProfileUpdateRequest req){
        return service.UpdateOrInsertForUser(me.getId(), req);
    }

    // Admin API
    @PostMapping("/{userId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public HostProfileResponse setStatus(@PathVariable Long userId, @PathVariable String status){
        return service.setStatus(userId, HostStatus.valueOf(status));
    }
}

