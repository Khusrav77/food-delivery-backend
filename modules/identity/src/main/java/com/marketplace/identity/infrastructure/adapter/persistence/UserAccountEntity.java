
package com.marketplace.identity.infrastructure.adapter.persistence;

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
@Table(name = "user_accounts")
public class UserAccountEntity {

    @Id
    private UUID id;

    @Column(name = "phone_number", nullable = false, unique = true, length = 32)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_account_roles",
            joinColumns = @JoinColumn(name = "user_account_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Set<Role> roles = new HashSet<>();

    protected UserAccountEntity() {
    }

    public UserAccountEntity(
            UUID id, String phoneNumber, AccountStatus status, Set<Role> roles) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.roles = new HashSet<>(roles);
    }

    public UUID getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }
}
