package com.marketplace.user.application.port.in;

import com.marketplace.user.api.contract.DeliveryAddressData;
import com.marketplace.user.api.contract.CheckoutProfileData;
import com.marketplace.user.domain.model.DeliveryAddressDetails;
import com.marketplace.user.domain.model.ProfileDetails;

import java.util.List;
import java.util.UUID;

public interface ManageUserProfileUseCase {
    CheckoutProfileData getProfile(UUID accountId);
    CheckoutProfileData updateProfile(UUID accountId, ProfileDetails details);
    List<DeliveryAddressData> getAddresses(UUID accountId);
    DeliveryAddressData addAddress(UUID accountId, DeliveryAddressDetails details, boolean makeDefault);
    DeliveryAddressData updateAddress(
            UUID accountId, UUID addressId, DeliveryAddressDetails details, boolean defaultAddress);
    void setDefaultAddress(UUID accountId, UUID addressId);
    void deleteAddress(UUID accountId, UUID addressId);
}
