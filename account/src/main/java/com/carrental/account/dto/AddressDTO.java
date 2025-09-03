package com.carrental.account.dto;

public record AddressDTO(String line1,
                         String line2,
                         String city,
                         String state,
                         String postalCode,
                         String countryCode) {}
