package com.shh.foodeliverybackendapp.modules.otp.service;

import com.shh.foodeliverybackendapp.modules.otp.storage.OtpStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.shh.foodeliverybackendapp.util.MaskingUtils.maskPhone;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final OtpStorage otpStorage;

    public OtpServiceImpl(OtpStorage otpStorage) {
        this.otpStorage = otpStorage;
    }

    @Override
    public void sendCode(String phone) {
        log.info("Generating OTP for phone={}", maskPhone(phone));
        String code = generateCode();

        otpStorage.save(phone, code, Duration.ofMinutes(3));
        log.info("OTP saved for phone={}, ttl={} minutes", maskPhone(phone), 3);
        log.debug("Generated OTP code: {}", code);
    }

    @Override
    public boolean verifyCode(String phone, String code) {

        Optional<String> savedCode = otpStorage.getCode(phone);

        if (savedCode.isEmpty()) {
            log.warn("OTP not found or expired for phone={}", maskPhone(phone));
            return false;
        }

        if (!savedCode.get().equals(code)) {
            log.warn("Invalid OTP for phone={}", maskPhone(phone));
            return false;
        }

        otpStorage.delete(phone);
        log.info("OTP successfully verified for phone={}", maskPhone(phone));

        return true;
    }

    private String generateCode() {
        return String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999));
    }
}
