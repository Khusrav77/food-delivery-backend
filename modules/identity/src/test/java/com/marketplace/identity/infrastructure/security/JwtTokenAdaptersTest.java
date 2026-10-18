package com.marketplace.identity.infrastructure.security;

import com.marketplace.identity.application.result.ValidatedToken;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenAdaptersTest {

    private static final String SECRET = "test-secret-key-with-more-than-thirty-two-bytes";
    private static final UserId USER_ID = new UserId(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    private JwtTokenProviderAdapter tokenProvider;
    private JwtTokenValidatorAdapter tokenValidator;
    private UserAccount account;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(
                SECRET, Duration.ofMinutes(15), Duration.ofDays(30));
        tokenProvider = new JwtTokenProviderAdapter(properties);
        tokenValidator = new JwtTokenValidatorAdapter(properties);
        account = UserAccount.create(USER_ID, new PhoneNumber("+12025550123"));
    }

    @Test
    void shouldGenerateAndValidateAccessToken() {
        var token = tokenProvider.generateAccessToken(account);

        ValidatedToken validated = tokenValidator.validateAccessToken(token.value());

        assertEquals(900, token.expiresInSeconds());
        assertEquals(USER_ID, validated.userId());
        assertEquals(account.roles(), validated.roles());
    }

    @Test
    void shouldGenerateAndValidateRefreshToken() {
        var token = tokenProvider.generateRefreshToken(account);

        ValidatedToken validated = tokenValidator.validateRefreshToken(token.value());

        assertEquals(Duration.ofDays(30).toSeconds(), token.expiresInSeconds());
        assertEquals(USER_ID, validated.userId());
        assertEquals(account.roles(), validated.roles());
    }

    @Test
    void shouldRejectRefreshTokenWhenAccessTokenIsExpected() {
        var token = tokenProvider.generateRefreshToken(account);

        assertThrows(IllegalArgumentException.class,
                () -> tokenValidator.validateAccessToken(token.value()));
    }

    @Test
    void shouldRejectAccessTokenWhenRefreshTokenIsExpected() {
        var token = tokenProvider.generateAccessToken(account);

        assertThrows(IllegalArgumentException.class,
                () -> tokenValidator.validateRefreshToken(token.value()));
    }

    @Test
    void shouldRejectTokenSignedWithAnotherKey() {
        JwtTokenValidatorAdapter validatorWithOtherKey = new JwtTokenValidatorAdapter(
                new JwtProperties("another-secret-key-with-more-than-thirty-two-bytes",
                        Duration.ofMinutes(15), Duration.ofDays(30)));
        var token = tokenProvider.generateAccessToken(account);

        assertThrows(IllegalArgumentException.class,
                () -> validatorWithOtherKey.validateAccessToken(token.value()));
    }

    @Test
    void shouldRejectTokenWithUnknownRole() {
        String token = Jwts.builder()
                .subject(USER_ID.value().toString())
                .claim("type", "access")
                .claim("roles", List.of("SUPERUSER"))
                .issuedAt(new Date())
                .expiration(Date.from(java.time.Instant.now().plusSeconds(60)))
                .signWith(JwtKeyProvider.createSigningKey(SECRET))
                .compact();

        assertThrows(IllegalArgumentException.class,
                () -> tokenValidator.validateAccessToken(token));
    }
}
