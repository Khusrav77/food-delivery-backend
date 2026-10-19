package com.marketplace.user.api.mapper;

import com.marketplace.user.api.contract.CheckoutProfileData;
import com.marketplace.user.api.contract.DeliveryAddressData;
import com.marketplace.user.domain.model.DeliveryAddress;
import com.marketplace.user.domain.model.DeliveryAddressDetails;
import com.marketplace.user.domain.model.ProfileDetails;
import com.marketplace.user.domain.model.UserProfile;

import java.util.List;

public final class UserProfileMapper {
    private UserProfileMapper() { }

    public static CheckoutProfileData toCheckoutData(UserProfile profile) {
        ProfileDetails details = profile.details();
        List<DeliveryAddressData> addresses = profile.addresses().stream()
                .map(UserProfileMapper::toAddressData)
                .toList();
        return new CheckoutProfileData(
                profile.accountId().value(),
                details.firstName(),
                details.lastName(),
                details.email(),
                details.yearOfBirth(),
                details.avatarUrl(),
                addresses,
                profile.hasCheckoutData());
    }

    public static DeliveryAddressData toAddressData(DeliveryAddress address) {
        DeliveryAddressDetails details = address.details();
        return new DeliveryAddressData(
                address.id().value(),
                details.label(),
                details.city(),
                details.street(),
                details.building(),
                details.apartment(),
                details.entrance(),
                details.floor(),
                details.intercom(),
                details.comment(),
                address.defaultAddress());
    }
}
