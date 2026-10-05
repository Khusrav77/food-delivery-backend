package com.marketplace.identity.infrastructure.security;

import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.domain.model.Role;
import com.marketplace.identity.domain.model.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Component
public final class JwtTokenProviderAdapter implements TokenProviderPort {

    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtTokenProviderAdapter(JwtProperties properties) {
        this.properties = Objects.requireNonNull(
                properties, "properties must not be null");
        this.signingKey = JwtKeyProvider.createSigningKey(properties.secretKey());
    }

    @Override
    public AccessToken generateAccessToken(UserAccount account) {
        Objects.requireNonNull(account, "account must not be null");
        long expiresInSeconds = properties.accessTokenTtl().toSeconds();
        String token = generateToken(account, expiresInSeconds, ACCESS_TOKEN_TYPE);
        return new AccessToken(token, expiresInSeconds);
    }

    @Override
    public RefreshToken generateRefreshToken(UserAccount account) {
        Objects.requireNonNull(account, "account must not be null");
        long expiresInSeconds = properties.refreshTokenTtl().toSeconds();
        String token = generateToken(account, expiresInSeconds, REFRESH_TOKEN_TYPE);
        return new RefreshToken(token, expiresInSeconds);
    }

    private String generateToken(
            UserAccount account,
            long expiresInSeconds,
            String tokenType
    ) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(expiresInSeconds);

        return Jwts.builder()
                .subject(account.id().value().toString())
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .claim("roles", account.roles()
                                .stream()
                                .map(Role::name)
                                .toList())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }
}
