package com.shh.foodeliverybackendapp.modules.user.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Role role = Role.USER;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_phone_verified", nullable = false)
    private boolean phoneVerified = false;

    @Column(name = "is_email_verified", nullable = false)
    private boolean emailVerified = false;

    protected User() {}

    public User(String phone) {this.phone = phone;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}

    public boolean isIsActive() {return isActive;}
    public void setIsActive(boolean active) {this.isActive = active;}

    public boolean isPhoneVerified() {return phoneVerified;}
    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public boolean isEmailVerified() {return emailVerified;}
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
