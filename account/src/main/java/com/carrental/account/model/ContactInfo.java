package com.carrental.account.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class ContactInfo {
    private String phoneNumber;

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
