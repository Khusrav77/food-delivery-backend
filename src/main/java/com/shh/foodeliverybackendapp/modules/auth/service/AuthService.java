package com.shh.foodeliverybackendapp.modules.auth.service;

import com.shh.foodeliverybackendapp.modules.auth.dto.response.AuthResponse;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.SendOtpRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.TokenRefreshRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.VerifyOtpRequest;

public interface AuthService {
    void sendOtp(SendOtpRequest phone);
    AuthResponse verifyOtp(VerifyOtpRequest request);
    AuthResponse refresh(TokenRefreshRequest request);
}
