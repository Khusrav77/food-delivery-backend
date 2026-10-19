package com.marketplace.user.domain.model;

import java.net.URI;
import java.time.Year;

public record ProfileDetails(
        String firstName,
        String lastName,
        String email,
        Integer yearOfBirth,
        String avatarUrl
) {
    public ProfileDetails {
        firstName = required(firstName, "first name", 120);
        lastName = required(lastName, "last name", 120);
        email = optional(email, "email", 254);
        avatarUrl = optional(avatarUrl, "avatar URL", 2048);

        if (email != null && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("email must be valid");
        }
        if (yearOfBirth != null
                && (yearOfBirth < 1900 || yearOfBirth > Year.now().getValue())) {
            throw new IllegalArgumentException("year of birth is out of range");
        }
        if (avatarUrl != null) {
            try {
                URI uri = URI.create(avatarUrl);
                if (!uri.isAbsolute() || !("https".equalsIgnoreCase(uri.getScheme())
                        || "http".equalsIgnoreCase(uri.getScheme()))) {
                    throw new IllegalArgumentException("avatar URL must be an absolute HTTP URL");
                }
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("avatar URL must be valid", exception);
            }
        }
    }

    private static String required(String value, String field, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        String normalized = value.trim();
        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException(field + " is too long");
        }
        return normalized;
    }

    private static String optional(String value, String field, int maxLength) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException(field + " is too long");
        }
        return normalized;
    }
}
