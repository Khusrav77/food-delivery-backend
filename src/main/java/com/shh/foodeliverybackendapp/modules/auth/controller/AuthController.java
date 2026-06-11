package com.shh.foodeliverybackendapp.modules.auth.controller;

import com.shh.foodeliverybackendapp.modules.auth.dto.*;
import com.shh.foodeliverybackendapp.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-code")
    void sendOtp(@Valid @RequestBody SendOtpRequest sendOtpRequest) {
        authService.sendOtp(sendOtpRequest);
    }

    @PostMapping("/verify")
    AuthResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
       return authService.verifyOtp(verifyOtpRequest);
    }

    @PostMapping("/refresh")
    AuthResponse refresh(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest);
    }

}
