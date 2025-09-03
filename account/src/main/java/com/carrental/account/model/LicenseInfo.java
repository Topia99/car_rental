package com.carrental.account.model;


import jakarta.persistence.Embeddable;

@Embeddable
public class LicenseInfo {
    private String number;
    private String issuingState;
    private java.time.LocalDate expiryDate;
    private Boolean verified;

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getIssuingState() { return issuingState; }
    public void setIssuingState(String issuingState) { this.issuingState = issuingState; }
    public java.time.LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(java.time.LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

}
