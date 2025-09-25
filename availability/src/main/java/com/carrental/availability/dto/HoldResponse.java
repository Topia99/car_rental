package com.carrental.availability.dto;

import java.time.Instant;

public record HoldResponse(
        String id,  // expose as string to clients
        Instant expiresAt
) {
}
