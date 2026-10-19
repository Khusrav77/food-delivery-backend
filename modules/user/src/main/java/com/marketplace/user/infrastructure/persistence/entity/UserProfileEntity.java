package com.marketplace.user.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "account_id", nullable = false, updatable = false, unique = true)
    private UUID accountId;

    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 120)
    private String lastName;

    @Column(length = 254)
    private String email;

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Column(name = "avatar_url", length = 2048)
    private String avatarUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<DeliveryAddressEntity> addresses = new ArrayList<>();

    protected UserProfileEntity() { }

    public UserProfileEntity(UUID id, UUID accountId, Instant now) {
        this.id = id;
        this.accountId = accountId;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void updateProfileFields(
            String firstName,
            String lastName,
            String email,
            Integer yearOfBirth,
            String avatarUrl,
            Instant updatedAt
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.yearOfBirth = yearOfBirth;
        this.avatarUrl = avatarUrl;
        this.updatedAt = updatedAt;
    }

    public List<DeliveryAddressEntity> addresses() { return addresses; }
    public UUID id() { return id; }
    public UUID accountId() { return accountId; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public String email() { return email; }
    public Integer yearOfBirth() { return yearOfBirth; }
    public String avatarUrl() { return avatarUrl; }
    public Instant createdAt() { return createdAt; }
}
