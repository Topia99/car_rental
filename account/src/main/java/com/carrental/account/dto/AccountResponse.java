package com.carrental.account.dto;

public record AccountResponse(Long id,
                              String username,
                              String email,
                              AddressDTO address,
                              ContactInfoDTO contact,
                              LicenseInfoDTO license) {
}
