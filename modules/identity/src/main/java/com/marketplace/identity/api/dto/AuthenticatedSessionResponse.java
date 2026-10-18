package com.marketplace.identity.api.dto;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedSessionResponse(
        UUID userId,
        Set<String> roles
) {
    public AuthenticatedSessionResponse {
        roles = Set.copyOf(roles);
    }
}
