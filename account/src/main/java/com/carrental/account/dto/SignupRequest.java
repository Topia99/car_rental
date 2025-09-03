package com.carrental.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(@NotBlank String username,
                                   @NotBlank @Email String email,
                                   @NotBlank @Size(min=6, max =20) String password,
                                   AddressDTO address,
                                   ContactInfoDTO contact,
                                   LicenseInfoDTO license) {

}
