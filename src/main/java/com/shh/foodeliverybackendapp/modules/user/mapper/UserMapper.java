package com.shh.foodeliverybackendapp.modules.user.mapper;

import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;

public class UserMapper {

    private UserMapper(){}

    public static void updateUser(User user, UpdateProfileRequest request) {
        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }

        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }

        if (request.gender() != null) {
            user.setGender(request.gender());
        }

        if (request.birthDate() != null) {
            user.setBirthDate(request.birthDate());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }
    }

   public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatarUrl(),
                user.getGender(),
                user.getBirthDate(),
                user.getPhone(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.isPhoneVerified(),
                user.isEmailVerified(),
                user.getCreatedAt());
    }
}
