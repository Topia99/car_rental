package com.carrental.availability.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name="availability_slot")
public class AvailabilitySlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public String listingId;
    @Column(nullable = false, columnDefinition="tstzrange")public String period;
    @Column(nullable = false) public String status;

    public String bookingId;
    public Instant expiresAt; // only for HOLD
}


