package com.shh.foodeliverybackendapp.modules.user.dto;

public record UserProfileResponse(
    String firstName,
    String lastName,
    String phone,
    String email,
    String birthDate,
    String avatarUrl,
    boolean phoneVerified
) {}