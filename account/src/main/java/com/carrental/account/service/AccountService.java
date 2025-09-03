package com.carrental.account.service;


import com.carrental.account.doa.AccountRepository;
import com.carrental.account.dto.AddressDTO;
import com.carrental.account.dto.SignupRequest;
import com.carrental.account.dto.UpdateAccountRequest;
import com.carrental.account.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class AccountService {
    private final AccountRepository repo;
    private final PasswordEncoder encoder;

    public AccountService(AccountRepository repo, PasswordEncoder encoder){
        this.repo = repo;
        this.encoder = encoder;
    }

    public Account signup(SignupRequest req){
        Optional<Account> existingAccount = repo.findByEmail(req.email());
        if(existingAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        Account a = new Account();
        a.setUsername(req.username());
        a.setEmail(req.email());
        a.setPasswordHash(encoder.encode(req.password()));

        if(req.address() != null) {
            a.setAddress(to(req.address()));
        }
        if(req.contact() != null) {
            a.setContact(to(req.contact()));
        }
        if(req.license() != null) {
            a.setLicense(to(req.license()));
        }

        return repo.save(a);
    }

    public Account get(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public Account getByEmail(String email){
        return repo.findByEmail(email).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public Account update(Long id, UpdateAccountRequest req) {
        Account a = get(id);
        if (req.username()!=null) a.setUsername(req.username());
        if (req.email()!=null)    a.setEmail(req.email());
        if(req.address()!=null) a.setAddress(to(req.address()));
        if(req.contact()!=null) a.setContact(to(req.contact()));
        if(req.license()!=null) a.setLicense(to(req.license()));

        return a;
    }

    public void delete(Long id){
        if(!repo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        repo.deleteById(id);
    }


    // --- Mapping helpers ---

    private Address to(com.carrental.account.dto.AddressDTO d){
        if(d==null) return null;

        var a = new Address();
        a.setLine1(d.line1());
        a.setLine2(d.line2());
        a.setCity(d.city());
        a.setState(d.state());
        a.setPostalCode(d.postalCode());
        a.setCountryCode(d.countryCode());

        return a;
    }

    private ContactInfo to(com.carrental.account.dto.ContactInfoDTO d) {
        if(d==null) return null;
        var c = new ContactInfo();
        c.setPhoneNumber(d.phoneNumber());

        return c;
    }

    private LicenseInfo to(com.carrental.account.dto.LicenseInfoDTO d){
        if(d==null) return null;
        var l = new LicenseInfo();
        l.setNumber(d.number());
        l.setIssuingState(d.issuingState());
        l.setExpiryDate(d.expiryDate());
        l.setVerified(Boolean.TRUE.equals(d.Verified()));

        return l;
    }

}
