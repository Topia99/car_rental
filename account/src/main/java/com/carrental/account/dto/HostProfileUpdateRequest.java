package com.carrental.account.dto;

import jakarta.validation.constraints.Size;

public record HostProfileUpdateRequest(
        @Size(max=60) String displayName,
        @Size(max=1000) String about,
        @Size(max=1000) String defaultPickupInstructions
) {
}
