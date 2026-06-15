package com.shh.foodeliverybackendapp.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request for verifying the OTP code")
public record VerifyOtpRequest(

        @Schema(
                description = "Phone number associated with the OTP code",
                example = "79991234567")
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\d{11}$",
                message = "Phone number must contain exactly 11 digits")
        String phone,

        @Schema(
                description = "Six-digit OTP code",
                example = "123456")
        @NotBlank(message = "OTP code is required")
        @Pattern(
                regexp = "^\\d{6}$",
                message = "OTP code must contain exactly 6 digits")
        String code

) {}