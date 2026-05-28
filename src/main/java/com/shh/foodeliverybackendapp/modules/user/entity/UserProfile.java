package com.shh.foodeliverybackendapp.modules.user.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Email
    @Column(name = "email", length = 50)
    private String email;

    protected UserProfile() {}

    public UserProfile(User user) {this.user = user;}
    public User getUser() {return user;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getAvatarUrl() {return avatarUrl;}
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}

    public Gender getGender() {return gender;}
    public void setGender(Gender gender) {this.gender = gender;}

    public LocalDate getBirthDate() {return birthDate;}
    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}
}