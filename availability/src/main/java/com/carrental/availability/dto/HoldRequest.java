package com.carrental.availability.dto;

public record HoldRequest(
        String listingId,
        String fromUtc,
        String toUtc,
        String status
) {
}
