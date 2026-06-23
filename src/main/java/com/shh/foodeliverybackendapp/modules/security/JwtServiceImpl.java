package com.shh.foodeliverybackendapp.modules.security;

import com.shh.foodeliverybackendapp.config.JwtProperties;
import com.shh.foodeliverybackendapp.modules.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TYPE = "type";

    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.secretKey()
                .getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(UUID userId, Role role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_ROLE, role)
                .claim(CLAIM_TYPE, TOKEN_TYPE_ACCESS)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(jwtProperties.accessTokenExpiration())))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(UUID userId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_TYPE, TOKEN_TYPE_REFRESH)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(jwtProperties.refreshTokenExpiration())))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean isAccessToken(String token) {
        return TOKEN_TYPE_ACCESS.equals(
                parseToken(token)
                        .getPayload()
                        .get(CLAIM_TYPE, String.class)
        );
    }

    @Override
    public boolean isRefreshToken(String token) {
        return TOKEN_TYPE_REFRESH.equals(
                parseToken(token)
                        .getPayload()
                        .get(CLAIM_TYPE, String.class));
    }

    @Override
    public UUID extractUserId(String token) {
        String subject = parseToken(token)
                .getPayload()
                .getSubject();

        return UUID.fromString(subject);
    }

    @Override
    public String extractRole(String token) {
        return parseToken(token)
                .getPayload()
                .get(CLAIM_ROLE, String.class);
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);
    }
}
