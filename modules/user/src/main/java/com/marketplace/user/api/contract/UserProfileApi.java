package com.marketplace.user.api.contract;

import java.util.Optional;

public interface UserProfileApi {
    Optional<CheckoutProfileData> findForCheckout(java.util.UUID accountId);
    CheckoutProfileData saveCheckoutProfile(SaveCheckoutProfile command);
}
