package com.marketplace.user.domain.model;

public record DeliveryAddressDetails(
        String label,
        String city,
        String street,
        String building,
        String apartment,
        String entrance,
        String floor,
        String intercom,
        String comment
) {
    public DeliveryAddressDetails {
        label = optional(label, "label", 80);
        city = required(city, "city", 120);
        street = required(street, "street", 180);
        building = required(building, "building", 40);
        apartment = optional(apartment, "apartment", 40);
        entrance = optional(entrance, "entrance", 40);
        floor = optional(floor, "floor", 40);
        intercom = optional(intercom, "intercom", 40);
        comment = optional(comment, "comment", 500);
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
