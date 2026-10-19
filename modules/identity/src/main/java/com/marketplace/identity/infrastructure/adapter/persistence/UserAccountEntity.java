package com.marketplace.identity.infrastructure.adapter.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "identity_user_accounts")
public class UserAccountEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "identity_user_account_roles",
            joinColumns = @JoinColumn(name = "user_account_id", nullable = false))
    @Column(name = "role", nullable = false, length = 32)
    private Set<String> roles = new HashSet<>();

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    protected UserAccountEntity() {
    }

    public UserAccountEntity(UUID id, String phone, Set<String> roles, String status) {
        this.id = id;
        this.phone = phone;
        this.roles = new HashSet<>(roles);
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public Set<String> getRoles() {
        return Set.copyOf(roles);
    }

    public String getStatus() {
        return status;
    }
}
