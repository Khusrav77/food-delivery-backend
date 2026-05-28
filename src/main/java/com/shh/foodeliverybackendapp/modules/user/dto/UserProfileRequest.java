package com.shh.foodeliverybackendapp.modules.user.dto;

import com.shh.foodeliverybackendapp.modules.user.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserProfileRequest(

        @Size(max = 20)
        String firstName,

        @Size(max = 20)
        String lastName,

        @Email
        @Size(max = 50)
        String email,

        String image,

        Gender gender,

        @Size(max = 20)
        String birthDate

) {}