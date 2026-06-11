package com.shh.foodeliverybackendapp.modules.user.dto;

import com.shh.foodeliverybackendapp.modules.user.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProfileRequest(

        @Size(max = 50)
        String firstName,

        @Size(max = 50)
        String lastName,

        String avatarUrl,
        Gender gender,
        LocalDate birthDate,

        @Email
        @Size(max = 50)
        String email

) {}