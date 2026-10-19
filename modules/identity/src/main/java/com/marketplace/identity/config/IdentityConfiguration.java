package com.marketplace.identity.config;

import com.marketplace.identity.application.port.in.AuthenticateUseCase;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.application.port.in.RefreshTokenUseCase;
import com.marketplace.identity.application.port.in.RequestOtpUseCase;
import com.marketplace.identity.application.port.in.VerifyOtpUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.EventPublisherPort;
import com.marketplace.identity.application.port.out.OtpGeneratorPort;
import com.marketplace.identity.application.port.out.OtpRequestRateLimiterPort;
import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.port.out.TokenValidatorPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.service.AuthenticationService;
import com.marketplace.identity.application.service.CreateUserAccountService;
import com.marketplace.identity.application.service.RefreshTokenService;
import com.marketplace.identity.application.service.RequestOtpService;
import com.marketplace.identity.application.service.VerifyOtpService;
import com.marketplace.identity.infrastructure.adapter.transaction.SpringTransactionalCreateUserAccountUseCase;
import com.marketplace.identity.infrastructure.adapter.otp.OtpRateLimitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties(OtpRateLimitProperties.class)
public class IdentityConfiguration {

    @Bean
    public CreateUserAccountService createUserAccountService(
            UserAccountPort userAccountPort,
            EventPublisherPort eventPublisherPort,
            ClockPort clockPort) {
        return new CreateUserAccountService(
                userAccountPort,
                eventPublisherPort,
                clockPort);
    }

    @Bean
    public CreateUserAccountUseCase createUserAccountUseCase(
            CreateUserAccountService createUserAccountService,
            PlatformTransactionManager transactionManager) {
        return new SpringTransactionalCreateUserAccountUseCase(
                createUserAccountService,
                transactionManager);
    }

    @Bean
    public RequestOtpUseCase requestOtpUseCase(
            OtpStoragePort otpStoragePort,
            OtpRequestRateLimiterPort otpRequestRateLimiterPort,
            OtpGeneratorPort otpGeneratorPort,
            ClockPort clockPort) {
        return new RequestOtpService(
                otpStoragePort,
                otpRequestRateLimiterPort,
                otpGeneratorPort,
                clockPort);
    }

    @Bean
    public VerifyOtpUseCase verifyOtpUseCase(
            OtpStoragePort otpStoragePort,
            UserAccountPort userAccountPort,
            CreateUserAccountUseCase createUserAccountUseCase,
            ClockPort clockPort) {
        return new VerifyOtpService(
                otpStoragePort,
                userAccountPort,
                createUserAccountUseCase,
                clockPort);
    }

    @Bean
    public AuthenticateUseCase authenticateUseCase(
            VerifyOtpUseCase verifyOtpUseCase,
            TokenProviderPort tokenProviderPort) {
        return new AuthenticationService(
                verifyOtpUseCase,
                tokenProviderPort);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(
            TokenValidatorPort tokenValidatorPort,
            UserAccountPort userAccountPort,
            TokenProviderPort tokenProviderPort) {
        return new RefreshTokenService(
                tokenValidatorPort,
                userAccountPort,
                tokenProviderPort);
    }
}
