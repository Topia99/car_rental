package com.carrental.account.dto;

import java.time.LocalDate;

public record LicenseInfoDTO (String number,
                              String issuingState,
                              LocalDate expiryDate,
                              Boolean Verified){
}
