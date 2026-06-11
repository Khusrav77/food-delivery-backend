package com.shh.foodeliverybackendapp.modules.auth.service;

import com.shh.foodeliverybackendapp.modules.auth.storage.OtpStorage;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpStorage otpStorage;

    public OtpServiceImpl(OtpStorage otpStorage) {
        this.otpStorage = otpStorage;
    }

    @Override
    public void sendCode(String phone) {

        String code = generateCode();

        otpStorage.save(phone, code, Duration.ofMinutes(3));
    }

    @Override
    public boolean verifyCode(String phone, String code) {

        Optional<String> savedCode = otpStorage.getCode(phone);

        if (savedCode.isEmpty() || !savedCode.get().equals(code)) {
            return false;
        }

       otpStorage.delete(phone);
       return true;
    }

    private String generateCode() {
        return String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999)
        );
    }
}
