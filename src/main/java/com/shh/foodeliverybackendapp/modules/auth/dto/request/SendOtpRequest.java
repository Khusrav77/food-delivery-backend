package com.shh.foodeliverybackendapp.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request for sending an OTP code to a phone number")
public record SendOtpRequest(

        @Schema(
                description = "Phone number in international or local format",
                example = "79991234567")
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\d{11}$",
                message = "Phone number must contain exactly 11 digits")
        String phone

) {}