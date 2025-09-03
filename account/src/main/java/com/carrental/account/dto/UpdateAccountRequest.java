package com.carrental.account.dto;

import jakarta.validation.constraints.Email;

public record UpdateAccountRequest(String username,
                                   @Email String email,
                                   AddressDTO address,
                                   ContactInfoDTO contact,
                                   LicenseInfoDTO license) {
}
