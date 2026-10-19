package com.marketplace.user.application.service;

import com.marketplace.user.api.contract.CheckoutProfileData;
import com.marketplace.user.api.contract.DeliveryAddressData;
import com.marketplace.user.api.contract.SaveCheckoutProfile;
import com.marketplace.user.api.contract.UserProfileApi;
import com.marketplace.user.api.mapper.UserProfileMapper;
import com.marketplace.user.application.exception.UserProfileNotFoundException;
import com.marketplace.user.application.port.in.ManageUserProfileUseCase;
import com.marketplace.user.application.port.out.UserProfileRepository;
import com.marketplace.user.domain.model.AccountId;
import com.marketplace.user.domain.model.DeliveryAddress;
import com.marketplace.user.domain.model.DeliveryAddressDetails;
import com.marketplace.user.domain.model.DeliveryAddressId;
import com.marketplace.user.domain.model.ProfileDetails;
import com.marketplace.user.domain.model.UserProfile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class UserProfileService implements UserProfileApi, ManageUserProfileUseCase {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckoutProfileData> findForCheckout(UUID accountId) {
        return repository.findByAccountId(new AccountId(accountId))
                .map(UserProfileMapper::toCheckoutData);
    }

    @Override
    public CheckoutProfileData saveCheckoutProfile(SaveCheckoutProfile command) {
        AccountId accountId = new AccountId(command.accountId());
        ProfileDetails details = toProfileDetails(command.firstName(), command.lastName(),
                command.email(), command.yearOfBirth(), command.avatarUrl());
        UserProfile profile = repository.findByAccountId(accountId)
                .map(existing -> {
                    existing.updateDetails(details);
                    return existing;
                })
                .orElseGet(() -> UserProfile.create(accountId, details));

        if (command.newAddress() != null) {
            profile.addAddress(toAddressDetails(command.newAddress()), command.makeAddressDefault());
        } else if (command.makeAddressDefault()) {
            throw new IllegalArgumentException("an address is required to set a default address");
        }
        return UserProfileMapper.toCheckoutData(repository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutProfileData getProfile(UUID accountId) {
        return UserProfileMapper.toCheckoutData(findProfile(accountId));
    }

    @Override
    public CheckoutProfileData updateProfile(UUID accountId, ProfileDetails details) {
        AccountId owner = new AccountId(accountId);
        UserProfile profile = repository.findByAccountId(owner)
                .map(existing -> {
                    existing.updateDetails(details);
                    return existing;
                })
                .orElseGet(() -> UserProfile.create(owner, details));
        return UserProfileMapper.toCheckoutData(repository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAddressData> getAddresses(UUID accountId) {
        return repository.findByAccountId(new AccountId(accountId))
                .map(UserProfile::addresses)
                .orElse(List.of())
                .stream()
                .map(UserProfileMapper::toAddressData)
                .toList();
    }

    @Override
    public DeliveryAddressData addAddress(
            UUID accountId,
            DeliveryAddressDetails details,
            boolean makeDefault
    ) {
        UserProfile profile = findProfile(accountId);
        DeliveryAddress address = profile.addAddress(details, makeDefault);
        repository.save(profile);
        return UserProfileMapper.toAddressData(address);
    }

    @Override
    public DeliveryAddressData updateAddress(
            UUID accountId,
            UUID addressId,
            DeliveryAddressDetails details,
            boolean defaultAddress
    ) {
        UserProfile profile = findProfile(accountId);
        DeliveryAddressId selectedId = new DeliveryAddressId(addressId);
        profile.updateAddress(selectedId, details);
        profile.setAddressDefault(selectedId, defaultAddress);
        DeliveryAddress address = profile.addresses().stream()
                .filter(item -> item.id().equals(selectedId))
                .findFirst()
                .orElseThrow();
        repository.save(profile);
        return UserProfileMapper.toAddressData(address);
    }

    @Override
    public void setDefaultAddress(UUID accountId, UUID addressId) {
        UserProfile profile = findProfile(accountId);
        profile.setDefaultAddress(new DeliveryAddressId(addressId));
        repository.save(profile);
    }

    @Override
    public void deleteAddress(UUID accountId, UUID addressId) {
        UserProfile profile = findProfile(accountId);
        profile.removeAddress(new DeliveryAddressId(addressId));
        repository.save(profile);
    }

    private UserProfile findProfile(UUID accountId) {
        return repository.findByAccountId(new AccountId(accountId))
                .orElseThrow(() -> new UserProfileNotFoundException(accountId));
    }

    private ProfileDetails toProfileDetails(
            String firstName,
            String lastName,
            String email,
            Integer yearOfBirth,
            String avatarUrl
    ) {
        return new ProfileDetails(firstName, lastName, email, yearOfBirth, avatarUrl);
    }

    private DeliveryAddressDetails toAddressDetails(SaveCheckoutProfile.DeliveryAddressInput address) {
        return new DeliveryAddressDetails(address.label(), address.city(), address.street(),
                address.building(), address.apartment(), address.entrance(), address.floor(),
                address.intercom(), address.comment());
    }
}
