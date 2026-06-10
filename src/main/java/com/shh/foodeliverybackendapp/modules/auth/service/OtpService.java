package com.shh.foodeliverybackendapp.modules.auth.service;

public interface OtpService {
    void sendCode(String phone);
    boolean verifyCode(String phone, String code);
}
