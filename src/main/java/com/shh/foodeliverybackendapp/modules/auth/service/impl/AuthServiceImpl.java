package com.shh.foodeliverybackendapp.modules.auth.service.impl;

import com.shh.foodeliverybackendapp.exception.InvalidOtpException;
import com.shh.foodeliverybackendapp.modules.auth.dto.AuthResponse;
import com.shh.foodeliverybackendapp.modules.auth.dto.TokenRefreshRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.UserVerifyResponse;
import com.shh.foodeliverybackendapp.modules.auth.dto.VerifyOtpRequest;
import com.shh.foodeliverybackendapp.modules.security.JwtService;
import com.shh.foodeliverybackendapp.modules.auth.service.AuthService;
import com.shh.foodeliverybackendapp.modules.auth.service.OtpService;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.mapper.UserMapper;
import com.shh.foodeliverybackendapp.modules.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final OtpService otpService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthServiceImpl(OtpService otpService, UserService userService, JwtService jwtService) {
        this.otpService = otpService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void sendOtp(String phone) {
        otpService.sendCode(phone);
    }

    @Override
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        boolean isValid = otpService.verifyCode(request.phone(), request.code());
        if (!isValid) {
            throw new InvalidOtpException("Invalid OTP code");
        }

        User user = userService.findOrCreateByPhone(request.phone());
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        UserVerifyResponse userVerifyResponse = UserMapper.toVerifyResponse(user);

        return new AuthResponse(accessToken,refreshToken,userVerifyResponse);
    }

    @Override
    public AuthResponse refresh(TokenRefreshRequest request) {
        String token = request.refreshToken();
        if (!jwtService.isTokenValid(token) || !jwtService.isRefreshToken(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UUID userId = jwtService.extractUserId(token);
        User user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(userId, user.getRole());
        String refreshToken = jwtService.generateRefreshToken(userId);

        UserVerifyResponse userVerifyResponse = UserMapper.toVerifyResponse(user);

        return new AuthResponse(accessToken, refreshToken,  userVerifyResponse);
    }
}
