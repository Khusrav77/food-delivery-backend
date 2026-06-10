package com.shh.foodeliverybackendapp.modules.auth.service;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public void sendCode(String phone) {

    }

    @Override
    public boolean verifyCode(String phone, String code) {
        return false;
    }
}
