package com.shh.foodeliverybackendapp.modules.user.dto;

import com.shh.foodeliverybackendapp.modules.user.entity.Role;

import java.util.UUID;

public record UserVerifyResponse(
        UUID uuid,
        String phone,
        Role role
) {}
