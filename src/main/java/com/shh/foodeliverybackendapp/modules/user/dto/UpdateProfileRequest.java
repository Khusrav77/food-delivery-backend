package com.shh.foodeliverybackendapp.modules.user.dto;

import com.shh.foodeliverybackendapp.modules.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request for updating the current user's profile")
public record UpdateProfileRequest(

        @Schema(
                description = "User first name",
                example = "John")
        @Size(max = 50)
        String firstName,

        @Schema(
                description = "User last name",
                example = "Doe")
        @Size(max = 50)
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
                description = "User email address",
                example = "john.doe@example.com")
        @Email
        @Size(max = 50)
        String email

) { }