package com.shh.foodeliverybackendapp.modules.user.service;


import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.repository.UserRopositotory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private final UserRopositotory userRepo;

    public UserServiceImpl(UserRopositotory userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserResponse getCurrentUser() {

        return null;
    }

    @Override
    public UserResponse findById(UUID id) {
        return null;
    }

    @Override
    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        return null;
    }

    @Override
    public User findOrCreateByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .orElseGet(() -> userRepo.save(new User(phone)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}
