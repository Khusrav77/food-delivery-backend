package com.shh.foodeliverybackendapp.modules.user.service;


import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.mapper.UserMapper;
import com.shh.foodeliverybackendapp.modules.user.repository.UserRopositotory;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User userId = getCurrentUserEntity();
        return UserMapper.toResponse(userId);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUserEntity();

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (existsByEmail(request.email())) {
                throw new EntityAlreadyExistsException("Email already in use: " + request.email());
            }
        }

        UserMapper.updateUser(user, request);
        User saved = userRepo.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    public User findOrCreateByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .orElseGet(() -> userRepo.save(new User(phone)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    private User getCurrentUserEntity() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserById(UUID.fromString(userId));
    }
}
