package com.marketplace.user.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class UserProfile {

    private final ProfileId id;
    private final AccountId accountId;
    private ProfileDetails details;
    private final List<DeliveryAddress> addresses;

    private UserProfile(
            ProfileId id,
            AccountId accountId,
            ProfileDetails details,
            List<DeliveryAddress> addresses
    ) {
        this.id = Objects.requireNonNull(id, "profile id must not be null");
        this.accountId = Objects.requireNonNull(accountId, "account id must not be null");
        this.details = Objects.requireNonNull(details, "profile details must not be null");
        this.addresses = new ArrayList<>(Objects.requireNonNull(addresses));
        ensureUniqueAddressIds(this.addresses);
        ensureAtMostOneDefault(this.addresses);
    }

    public static UserProfile create(AccountId accountId, ProfileDetails details) {
        return new UserProfile(ProfileId.generate(), accountId, details, List.of());
    }

    public static UserProfile reconstitute(
            ProfileId id,
            AccountId accountId,
            ProfileDetails details,
            List<DeliveryAddress> addresses
    ) {
        return new UserProfile(id, accountId, details, addresses);
    }

    public void updateDetails(ProfileDetails details) {
        this.details = Objects.requireNonNull(details, "profile details must not be null");
    }

    public DeliveryAddress addAddress(DeliveryAddressDetails details, boolean defaultAddress) {
        if (defaultAddress) {
            clearDefaultAddress();
        }
        DeliveryAddress address = new DeliveryAddress(
                DeliveryAddressId.generate(), details, defaultAddress);
        addresses.add(address);
        return address;
    }

    public DeliveryAddress updateAddress(DeliveryAddressId addressId, DeliveryAddressDetails details) {
        int index = addressIndex(addressId);
        DeliveryAddress updated = addresses.get(index).update(details);
        addresses.set(index, updated);
        return updated;
    }

    public void setDefaultAddress(DeliveryAddressId addressId) {
        setAddressDefault(addressId, true);
    }

    public void setAddressDefault(DeliveryAddressId addressId, boolean defaultAddress) {
        int selectedIndex = addressIndex(addressId);
        if (defaultAddress) {
            clearDefaultAddress();
        }
        for (int index = 0; index < addresses.size(); index++) {
            DeliveryAddress address = addresses.get(index);
            if (index == selectedIndex || defaultAddress) {
                addresses.set(index, address.withDefault(index == selectedIndex && defaultAddress));
            }
        }
    }

    public void removeAddress(DeliveryAddressId addressId) {
        addresses.remove(addressIndex(addressId));
    }

    public boolean hasCheckoutData() {
        return !addresses.isEmpty();
    }

    public Optional<DeliveryAddress> defaultAddress() {
        return addresses.stream().filter(DeliveryAddress::defaultAddress).findFirst();
    }

    public ProfileId id() { return id; }
    public AccountId accountId() { return accountId; }
    public ProfileDetails details() { return details; }
    public List<DeliveryAddress> addresses() { return List.copyOf(addresses); }

    private void clearDefaultAddress() {
        for (int index = 0; index < addresses.size(); index++) {
            addresses.set(index, addresses.get(index).withDefault(false));
        }
    }

    private int addressIndex(DeliveryAddressId addressId) {
        Objects.requireNonNull(addressId, "address id must not be null");
        for (int index = 0; index < addresses.size(); index++) {
            if (addresses.get(index).id().equals(addressId)) {
                return index;
            }
        }
        throw new AddressNotFoundException(addressId);
    }

    private static void ensureUniqueAddressIds(List<DeliveryAddress> addresses) {
        long uniqueCount = addresses.stream().map(DeliveryAddress::id).distinct().count();
        if (uniqueCount != addresses.size()) {
            throw new IllegalArgumentException("delivery address ids must be unique");
        }
    }

    private static void ensureAtMostOneDefault(List<DeliveryAddress> addresses) {
        if (addresses.stream().filter(DeliveryAddress::defaultAddress).count() > 1) {
            throw new IllegalArgumentException("profile can have at most one default address");
        }
    }
}
