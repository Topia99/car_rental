package com.carrental.account.controller;

import com.carrental.account.dto.*;
import com.carrental.account.model.Account;
import com.carrental.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService service;
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse signup(@Valid @RequestBody SignupRequest request){
        Account a = service.signup(request);
        return toResponse(a);
    }

    @GetMapping("/me")
    public AccountResponse me(Authentication auth) {
        String email = auth.getName();
        return toResponse(service.getByEmail(email));
    }

    @PutMapping("/me")
    public AccountResponse updateMe(Authentication auth, @Valid @RequestBody UpdateAccountRequest req){
        String email = auth.getName();
        Account me = service.getByEmail(email);
        Account updatedMe = service.update(me.getId(), req);
        return toResponse(updatedMe);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(Authentication auth){
        String email = auth.getName();
        Account me = service.getByEmail(email);
        service.delete(me.getId());
    }


    // --- Helper Method ---
    private AccountResponse toResponse(Account a){
        var addr = a.getAddress();
        var con = a.getContact();
        var lic = a.getLicense();
        return new AccountResponse(
                a.getId(),
                a.getUsername(),
                a.getEmail(),
                addr==null? null : new AddressDTO(addr.getLine1(), addr.getLine2(), addr.getCity(), addr.getState(), addr.getPostalCode(), addr.getCountryCode()),
                con==null? null : new ContactInfoDTO(con.getPhoneNumber()),
                lic==null? null : new LicenseInfoDTO(lic.getNumber(), lic.getIssuingState(), lic.getExpiryDate(), lic.getVerified())
        );
    }

}