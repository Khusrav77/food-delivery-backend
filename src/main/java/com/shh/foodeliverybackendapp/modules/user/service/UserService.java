package com.shh.foodeliverybackendapp.modules.user.service;

import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;

import java.util.UUID;

public interface UserService {
    UserResponse getCurrentUser();

    User getUserById(UUID id);

    UserResponse updateProfile(UpdateProfileRequest request);

    User findOrCreateByPhone(String phone);

    boolean existsByEmail(String email);

    User getCurrentUserEntity();
}
