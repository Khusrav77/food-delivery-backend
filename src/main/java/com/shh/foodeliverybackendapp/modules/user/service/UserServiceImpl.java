package com.shh.foodeliverybackendapp.modules.user.service;


import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.repository.UserRopositotory;

import java.util.UUID;

public class UserServiceImpl implements UserService{

    private UserRopositotory userRepo;

    @Override
    public UserResponse getCurrentUser() {

        return null;
    }

    @Override
    public UserResponse findById(UUID id) {
        return null;
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        return null;
    }

    @Override
    public User findByPhone(String phone) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}
