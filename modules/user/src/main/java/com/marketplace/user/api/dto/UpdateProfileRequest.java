package com.marketplace.user.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 120) String firstName,
        @NotBlank @Size(max = 120) String lastName,
        @Email @Size(max = 254) String email,
        @Min(1900) Integer yearOfBirth,
        @Size(max = 2048) String avatarUrl
) { }
