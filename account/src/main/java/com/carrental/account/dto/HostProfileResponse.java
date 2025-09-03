package com.carrental.account.dto;

public record HostProfileResponse(
        Long id,
        Long userId,
        String status,
        String displayName,
        String about,
        String defaultPickupInstructions
) {
}
