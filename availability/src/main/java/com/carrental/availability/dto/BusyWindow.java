package com.carrental.availability.dto;

import java.time.Instant;

public record BusyWindow(
        Instant from,
        Instant to,
        String status
) {
}
