package com.shh.foodeliverybackendapp.modules.user.dto;

import com.shh.foodeliverybackendapp.modules.user.entity.Gender;
import com.shh.foodeliverybackendapp.modules.user.entity.Role;

import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID id,
        String firstName,
        String lastName,
        String avatarUrl,
        Gender gender,
        LocalDate birthDate,
        String phone,
        String email,
        Role role,

        boolean active,
        boolean phoneVerified,
        boolean emailVerified,

        Instant createdAt

) {}