package com.shh.foodeliverybackendapp.modules.user.dto;

import com.shh.foodeliverybackendapp.modules.user.entity.Gender;
import com.shh.foodeliverybackendapp.modules.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Current user profile information")
public record UserResponse(

        @Schema(
                description = "User unique identifier",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(
                description = "User first name",
                example = "John")
        String firstName,

        @Schema(
                description = "User last name",
                example = "Doe")
        String lastName,

        @Schema(
                description = "URL of the user's avatar",
                example = "https://cdn.example.com/avatars/user-1.jpg")
        String avatarUrl,

        @Schema(
                description = "User gender",
                example = "MALE")
        Gender gender,

        @Schema(
                description = "User date of birth",
                example = "1995-08-20")
        LocalDate birthDate,

        @Schema(
                description = "User phone number",
                example = "79991234567")
        String phone,

        @Schema(
                description = "User email address",
                example = "john.doe@example.com")
        String email,

        @Schema(
                description = "User role",
                example = "USER")
        Role role,

        @Schema(
                description = "Indicates whether the account is active",
                example = "true")
        boolean active,

        @Schema(
                description = "Indicates whether the phone number has been verified",
                example = "true")
        boolean phoneVerified,

        @Schema(
                description = "Indicates whether the email address has been verified",
                example = "false")
        boolean emailVerified,

        @Schema(
                description = "Account creation timestamp",
                example = "2026-06-15T10:30:00Z")
        Instant createdAt

) { }