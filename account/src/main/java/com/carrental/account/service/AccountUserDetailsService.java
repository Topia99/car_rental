package com.carrental.account.service;

import com.carrental.account.config.AccountPrincipal;
import com.carrental.account.doa.AccountRepository;
import com.carrental.account.model.Account;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountUserDetailsService implements UserDetailsService {
    private final AccountRepository repo;
    public AccountUserDetailsService(AccountRepository repo){
        this.repo = repo;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account a = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authorities = a.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();

        return new AccountPrincipal(
                a.getId(),
                a.getEmail(),
                a.getPasswordHash(),
                authorities
        );
    }
}
