package com.shh.foodeliverybackendapp.modules.user.repository;

import com.shh.foodeliverybackendapp.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRopositotory extends JpaRepository<User, UUID> {
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
}
