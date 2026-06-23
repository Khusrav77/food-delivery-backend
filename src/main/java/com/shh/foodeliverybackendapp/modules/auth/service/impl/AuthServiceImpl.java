package com.shh.foodeliverybackendapp.modules.auth.service.impl;

import com.shh.foodeliverybackendapp.exception.InvalidOtpException;
import com.shh.foodeliverybackendapp.exception.InvalidRefreshTokenException;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.SendOtpRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.TokenRefreshRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.request.VerifyOtpRequest;
import com.shh.foodeliverybackendapp.modules.auth.dto.response.AuthResponse;
import com.shh.foodeliverybackendapp.modules.auth.dto.response.UserVerifyResponse;
import com.shh.foodeliverybackendapp.modules.security.JwtService;
import com.shh.foodeliverybackendapp.modules.auth.service.AuthService;
import com.shh.foodeliverybackendapp.modules.otp.service.OtpService;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.mapper.UserMapper;
import com.shh.foodeliverybackendapp.modules.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.shh.foodeliverybackendapp.util.MaskingUtils.maskPhone;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final OtpService otpService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthServiceImpl(OtpService otpService, UserService userService, JwtService jwtService) {
        this.otpService = otpService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void sendOtp(SendOtpRequest request) {
        log.info("OTP requested for phone={}", maskPhone(request.phone()));

        otpService.sendCode(request.phone());

        log.info("OTP sent for phone={}", maskPhone(request.phone()));
    }

    @Override
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        log.info("OTP verification requested for phone={}", maskPhone(request.phone()));
        boolean isValid = otpService.verifyCode(request.phone(), request.code());

        if (!isValid) {
            log.warn("OTP verification failed for phone={}", maskPhone(request.phone()));
            throw new InvalidOtpException("Invalid OTP code");
        }

        User user = userService.findOrCreateByPhone(request.phone());
        log.info("User authenticated: userId={}, role={}", user.getId(), user.getRole());

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        UserVerifyResponse userVerifyResponse = UserMapper.toVerifyResponse(user);

        return new AuthResponse(accessToken,refreshToken,userVerifyResponse);
    }

    @Override
    public AuthResponse refresh(TokenRefreshRequest request) {
        log.debug("Refresh token request received");
        String token = request.refreshToken();

        if (!jwtService.isTokenValid(token) || !jwtService.isRefreshToken(token)) {
            log.warn("Invalid refresh token");
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        UUID userId = jwtService.extractUserId(token);
        log.info("Refreshing access token for userId={}", userId);

        User user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(userId, user.getRole());
        String refreshToken = jwtService.generateRefreshToken(userId);

        UserVerifyResponse userVerifyResponse = UserMapper.toVerifyResponse(user);

        return new AuthResponse(accessToken, refreshToken,  userVerifyResponse);
    }
}
