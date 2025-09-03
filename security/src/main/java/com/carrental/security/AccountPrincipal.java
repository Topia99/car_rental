package com.carrental.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AccountPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final List<? extends GrantedAuthority> authorities;

    public AccountPrincipal(Long id, String email, String passwordHash, List<? extends GrantedAuthority> authorities){
        this.id=id;
        this.email=email;
        this.passwordHash=passwordHash;
        this.authorities=authorities;
    }

    public Long getId() { return id;}
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
