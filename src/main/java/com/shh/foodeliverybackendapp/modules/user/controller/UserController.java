package com.shh.foodeliverybackendapp.modules.user.controller;


import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping
    public UserResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }

}
