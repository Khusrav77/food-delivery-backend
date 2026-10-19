package com.marketplace.identity.infrastructure.adapter.otp;

import com.marketplace.identity.application.port.out.OtpRequestRateLimitDecision;
import com.marketplace.identity.application.port.out.OtpRequestRateLimiterPort;
import com.marketplace.identity.domain.model.PhoneNumber;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public final class RedisOtpRequestRateLimiterAdapter implements OtpRequestRateLimiterPort {

    private static final long MINUTE_MILLIS = 60_000L;
    private static final long FIFTEEN_MINUTES_MILLIS = 15 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * 60 * MINUTE_MILLIS;
    private static final RedisScript<Long> RATE_LIMIT_SCRIPT = script("redis/rate-limit-otp.lua");

    private final StringRedisTemplate redisTemplate;
    private final OtpRateLimitProperties properties;

    public RedisOtpRequestRateLimiterAdapter(
            StringRedisTemplate redisTemplate,
            OtpRateLimitProperties properties) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
    }

    @Override
    public OtpRequestRateLimitDecision tryAcquire(PhoneNumber phone, String clientIp) {
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(clientIp, "clientIp must not be null");
        if (clientIp.isBlank()) {
            throw new IllegalArgumentException("clientIp must not be blank");
        }

        String phoneKey = digest(phone.value());
        String ipKey = digest(clientIp);
        List<String> keys = List.of(
                "identity:otp:limit:phone:minute:" + phoneKey,
                "identity:otp:limit:phone:15m:" + phoneKey,
                "identity:otp:limit:ip:15m:" + ipKey,
                "identity:otp:limit:ip:day:" + ipKey);

        Long retryAfter = redisTemplate.execute(
                RATE_LIMIT_SCRIPT,
                keys,
                UUID.randomUUID().toString(),
                Integer.toString(properties.perPhoneRequestsPerMinute()),
                Long.toString(MINUTE_MILLIS),
                Integer.toString(properties.perPhoneRequestsPer15Minutes()),
                Long.toString(FIFTEEN_MINUTES_MILLIS),
                Integer.toString(properties.perIpRequestsPer15Minutes()),
                Long.toString(FIFTEEN_MINUTES_MILLIS),
                Integer.toString(properties.perIpRequestsPerDay()),
                Long.toString(DAY_MILLIS));

        if (retryAfter == null) {
            throw new IllegalStateException("Could not apply OTP request rate limits");
        }
        return retryAfter == 0
                ? OtpRequestRateLimitDecision.permitted()
                : OtpRequestRateLimitDecision.denied(retryAfter);
    }

    private static String digest(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }

    private static RedisScript<Long> script(String path) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource(path));
        script.setResultType(Long.class);
        return script;
    }
}
