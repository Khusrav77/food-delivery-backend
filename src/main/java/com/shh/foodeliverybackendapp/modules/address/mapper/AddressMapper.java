package com.shh.foodeliverybackendapp.modules.address.mapper;

import com.shh.foodeliverybackendapp.modules.address.dto.UserAddressRequest;
import com.shh.foodeliverybackendapp.modules.address.dto.UserAddressResponse;
import com.shh.foodeliverybackendapp.modules.address.entity.UserAddress;
import com.shh.foodeliverybackendapp.modules.user.entity.User;

public final class AddressMapper {

    private AddressMapper() {}

    public static UserAddress toUserAddress(User user,UserAddressRequest request) {
        UserAddress address = new UserAddress();
        address.setUser(user);
        updateAddress(address, request);
        return address;
    }

    public static void updateAddress(UserAddress userAddress, UserAddressRequest request) {
        userAddress.setTitle(request.title());
        userAddress.setCity(request.city());
        userAddress.setStreet(request.street());
        userAddress.setHouse(request.house());
        userAddress.setApartment(request.apartment());
        userAddress.setEntrance(request.entrance());
        userAddress.setFloor(request.floor());
        userAddress.setIntercom(request.intercom());
        userAddress.setComment(request.comment());
        userAddress.setLatitude(request.latitude());
        userAddress.setLongitude(request.longitude());
        userAddress.setDefault(Boolean.TRUE.equals(request.isDefault()));
    }

    public static UserAddressResponse toUserAddressResponse(UserAddress address) {
        return new UserAddressResponse(
                address.getId(),
                address.getTitle(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment(),
                address.getEntrance(),
                address.getFloor(),
                address.getIntercom(),
                address.getComment(),
                address.getLatitude(),
                address.getLongitude(),
                address.isDefault(),
                address.getCreatedAt(),
                address.getUpdatedAt());
    }
}
