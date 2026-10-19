package com.marketplace.identity.infrastructure.adapter.otp;

import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

@Component
public final class RedisOtpStorageAdapter implements OtpStoragePort {

    private static final RedisScript<Long> SAVE_OTP_SCRIPT = script("redis/save-otp.lua");
    private static final RedisScript<Long> VERIFY_OTP_SCRIPT = script("redis/verify-otp.lua");
    private final StringRedisTemplate redisTemplate;

    public RedisOtpStorageAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
    }

    @Override
    public void save(PhoneNumber phone, OtpCode otp) {
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(otp, "otp must not be null");

        Long saved = redisTemplate.execute(
                SAVE_OTP_SCRIPT,
                List.of(otpKey(phone)),
                otp.value(),
                Long.toString(otp.expiresAt().toEpochMilli()));
        if (saved == null || saved != 1L) {
            throw new IllegalStateException("Could not store OTP");
        }
    }

    @Override
    public boolean verify(PhoneNumber phone, String candidate, Instant now) {
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(candidate, "candidate must not be null");
        Objects.requireNonNull(now, "now must not be null");

        Long verified = redisTemplate.execute(
                VERIFY_OTP_SCRIPT,
                List.of(otpKey(phone)),
                candidate,
                Long.toString(now.toEpochMilli()),
                Integer.toString(OtpCode.MAX_VERIFICATION_ATTEMPTS));
        return verified != null && verified == 1L;
    }

    private static String otpKey(PhoneNumber phone) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(phone.value().getBytes(StandardCharsets.UTF_8));
            return "identity:otp:code:" + HexFormat.of().formatHex(digest);
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
