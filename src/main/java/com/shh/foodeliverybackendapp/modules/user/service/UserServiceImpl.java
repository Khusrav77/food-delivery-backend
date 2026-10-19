package com.shh.foodeliverybackendapp.modules.user.service;


import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.mapper.UserMapper;
import com.shh.foodeliverybackendapp.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
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
                .orElseThrow(() -> {throw  new EntityNotFoundException("User", id);});
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUserEntity();

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (existsByEmail(request.email())) {
                throw new EntityAlreadyExistsException("Email already in use");
            }
        }

        UserMapper.updateUser(user, request);
        User saved = userRepo.save(user);
        log.info("User profile updated: userId={}", saved.getId());
        return UserMapper.toResponse(saved);
    }

    @Override
    public User findOrCreateByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .orElseGet(() -> {
                   User user = userRepo.save(new User(phone));
                   log.info("New user created: userId={}", user.getId());
                   return user;
                });
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public User getCurrentUserEntity() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserById(UUID.fromString(userId));
    }
}
