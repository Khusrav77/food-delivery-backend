package com.marketplace.user.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_delivery_addresses")
public class DeliveryAddressEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_delivery_addresses_profile"))
    private UserProfileEntity profile;

    @Column(length = 80)
    private String label;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 180)
    private String street;

    @Column(nullable = false, length = 40)
    private String building;

    @Column(length = 40)
    private String apartment;

    @Column(length = 40)
    private String entrance;

    @Column(length = 40)
    private String floor;

    @Column(length = 40)
    private String intercom;

    @Column(length = 500)
    private String comment;

    @Column(name = "is_default", nullable = false)
    private boolean defaultAddress;

    protected DeliveryAddressEntity() { }

    public DeliveryAddressEntity(UUID id, UserProfileEntity profile) {
        this.id = id;
        this.profile = profile;
    }

    public void update(
            UserProfileEntity profile,
            String label,
            String city,
            String street,
            String building,
            String apartment,
            String entrance,
            String floor,
            String intercom,
            String comment,
            boolean defaultAddress
    ) {
        this.profile = profile;
        this.label = label;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.entrance = entrance;
        this.floor = floor;
        this.intercom = intercom;
        this.comment = comment;
        this.defaultAddress = defaultAddress;
    }

    public UUID id() { return id; }
    public String label() { return label; }
    public String city() { return city; }
    public String street() { return street; }
    public String building() { return building; }
    public String apartment() { return apartment; }
    public String entrance() { return entrance; }
    public String floor() { return floor; }
    public String intercom() { return intercom; }
    public String comment() { return comment; }
    public boolean defaultAddress() { return defaultAddress; }
}
