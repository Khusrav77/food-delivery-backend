package com.shh.foodeliverybackendapp.modules.base;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AbstractEntity() {}

    public UUID getId() {return id;}

    public Instant getCreatedAt() {return createdAt;}

    public Instant getUpdatedAt() {return updatedAt;}

    @Override
    public final boolean equals(Object o) {

        if (this == o) return true;

        if (o == null) return false;

        if (Hibernate.getClass(this)
                != Hibernate.getClass(o)) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}