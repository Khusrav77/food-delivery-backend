package com.shh.foodeliverybackendapp.modules.auth.service;

public class OtpServiceImpl implements OtpService {
    @Override
    public void sendCode(String phone) {

    }

    @Override
    public boolean verifyCode(String phone, String code) {
        return false;
    }
}
