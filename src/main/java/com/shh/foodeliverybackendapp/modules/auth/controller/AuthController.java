package com.shh.foodeliverybackendapp.modules.auth.controller;

import com.shh.foodeliverybackendapp.modules.auth.dto.request.SendOtpRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.TokenRefreshRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.VerifyOtpRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.response.AuthResponse;
import com.shh.foodeliverybackendapp.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Send OTP code",
            description = "Generates and sends a one-time password (OTP) to the specified phone number.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid phone number format", content = @Content)
    })
    @PostMapping("/send-code")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendOtp(@Valid @RequestBody SendOtpRequest sendOtpRequest) {
        authService.sendOtp(sendOtpRequest);
    }

    @Operation(
            summary = "Verify OTP code",
            description = "Verifies the OTP code and returns access and refresh tokens.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP code", content = @Content)
    })
    @PostMapping("/verify")
    public AuthResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return authService.verifyOtp(verifyOtpRequest);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token", content = @Content)
    })
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest);
    }
}