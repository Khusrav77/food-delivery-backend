package com.marketplace.user.api.controller;

import com.marketplace.user.api.contract.CheckoutProfileData;
import com.marketplace.user.api.contract.DeliveryAddressData;
import com.marketplace.user.api.dto.DeliveryAddressRequest;
import com.marketplace.user.api.dto.UpdateProfileRequest;
import com.marketplace.user.application.port.in.ManageUserProfileUseCase;
import com.marketplace.user.domain.model.DeliveryAddressDetails;
import com.marketplace.user.domain.model.ProfileDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final ManageUserProfileUseCase useCase;

    public UserProfileController(ManageUserProfileUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public CheckoutProfileData getProfile(Authentication authentication) {
        return useCase.getProfile(accountId(authentication));
    }

    @PutMapping
    public CheckoutProfileData updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        ProfileDetails details = new ProfileDetails(request.firstName(), request.lastName(),
                request.email(), request.yearOfBirth(), request.avatarUrl());
        return useCase.updateProfile(accountId(authentication), details);
    }

    @GetMapping("/addresses")
    public List<DeliveryAddressData> getAddresses(Authentication authentication) {
        return useCase.getAddresses(accountId(authentication));
    }

    @PostMapping("/addresses")
    public ResponseEntity<DeliveryAddressData> addAddress(
            @Valid @RequestBody DeliveryAddressRequest request,
            Authentication authentication
    ) {
        DeliveryAddressData created = useCase.addAddress(
                accountId(authentication), toDetails(request), request.defaultAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/addresses/{addressId}")
    public DeliveryAddressData updateAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody DeliveryAddressRequest request,
            Authentication authentication
    ) {
        return useCase.updateAddress(
                accountId(authentication), addressId, toDetails(request), request.defaultAddress());
    }

    @PutMapping("/addresses/{addressId}/default")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setDefaultAddress(
            @PathVariable UUID addressId,
            Authentication authentication
    ) {
        useCase.setDefaultAddress(accountId(authentication), addressId);
    }

    @DeleteMapping("/addresses/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(
            @PathVariable UUID addressId,
            Authentication authentication
    ) {
        useCase.deleteAddress(accountId(authentication), addressId);
    }

    private UUID accountId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    private DeliveryAddressDetails toDetails(DeliveryAddressRequest request) {
        return new DeliveryAddressDetails(request.label(), request.city(), request.street(),
                request.building(), request.apartment(), request.entrance(), request.floor(),
                request.intercom(), request.comment());
    }
}
