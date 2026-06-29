package com.shh.foodeliverybackendapp.modules.address.service;

import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.address.dto.UserAddressRequest;
import com.shh.foodeliverybackendapp.modules.address.dto.UserAddressResponse;
import com.shh.foodeliverybackendapp.modules.address.entity.UserAddress;
import com.shh.foodeliverybackendapp.modules.address.mapper.AddressMapper;
import com.shh.foodeliverybackendapp.modules.address.repository.UserAddressRepository;
import com.shh.foodeliverybackendapp.modules.base.CrudAbstractService;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserAddressServiceImpl implements CrudAbstractService<UserAddressRequest, UserAddressResponse> {

    private final UserAddressRepository userAddressRepo;
    private final UserService userService;

    public UserAddressServiceImpl(UserAddressRepository userAddressRepo, UserService userService) {
        this.userAddressRepo = userAddressRepo;
        this.userService = userService;
    }

    @Override
    public UserAddressResponse create(UserAddressRequest request) {
        User user = userService.getCurrentUserEntity();
        UserAddress userAddress = AddressMapper.toUserAddress(user, request);
        UserAddress saved = userAddressRepo.save(userAddress);
        return AddressMapper.toUserAddressResponse(saved);
    }

    @Override
    public UserAddressResponse findById(UUID id) {
        return AddressMapper.toUserAddressResponse(getAddress(id)) ;
    }

    @Override
    public List<UserAddressResponse> findAll() {
        User currentUser = userService.getCurrentUserEntity();

        return userAddressRepo.findAllByUser(currentUser)
                .stream()
                .map(AddressMapper::toUserAddressResponse)
                .toList();
    }

    @Override
    public UserAddressResponse updateById(UUID id, UserAddressRequest request) {
        UserAddress userAddress = getAddress(id);
        AddressMapper.updateAddress(userAddress, request);
        UserAddress saved = userAddressRepo.save(userAddress);
        return AddressMapper.toUserAddressResponse(saved);
    }

    @Override
    public void deleteById(UUID id) {
        userAddressRepo.delete(getAddress(id));
    }

    private UserAddress getAddress(UUID id) {
        User currentUser = userService.getCurrentUserEntity();
        return userAddressRepo.findByIdAndUser(id,currentUser).orElseThrow(() ->
                new EntityNotFoundException("Address not found", id));
    }
}
