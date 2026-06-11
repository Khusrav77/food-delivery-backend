package com.shh.foodeliverybackendapp.modules.auth.service;

import com.shh.foodeliverybackendapp.modules.auth.dto.AuthResponse;
import com.shh.foodeliverybackendapp.modules.auth.dto.SendOtpRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.TokenRefreshRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.VerifyOtpRequest;

public interface AuthService {
    void sendOtp(SendOtpRequest phone);
    AuthResponse verifyOtp(VerifyOtpRequest request);
    AuthResponse refresh(TokenRefreshRequest request);
}
