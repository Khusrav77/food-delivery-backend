package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.UUID;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public  class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "position", nullable = false)
    private Integer position = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AbstractEntity() {}

    public UUID getId() {return id;}

    public Integer getPosition() {return position;}

    public void setPosition(Integer position) {
        this.position = position;}

    public Instant getCreatedAt() {return createdAt;}

    public Instant getUpdatedAt() {return updatedAt;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
