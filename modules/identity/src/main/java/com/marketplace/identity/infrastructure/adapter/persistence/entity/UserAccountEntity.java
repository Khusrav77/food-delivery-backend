
package com.marketplace.identity.infrastructure.adapter.persistence.entity;

import com.marketplace.identity.domain.model.AccountStatus;
import com.marketplace.identity.domain.model.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "phone", nullable = false, unique = true, length = 32)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private AccountStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "identity_user_account_roles",
            joinColumns = @JoinColumn(name = "user_account_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 32)
    private Set<Role> roles = new HashSet<>();

    protected UserAccountEntity() {
        // Required by JPA
    }

    public UserAccountEntity(
            UUID id,
            String phone,
            AccountStatus status,
            Set<Role> roles
    ) {
        this.id = id;
        this.phone = phone;
        this.status = status;
        this.roles = new HashSet<>(roles);
    }

    public UUID getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = new HashSet<>(roles);
    }
}
