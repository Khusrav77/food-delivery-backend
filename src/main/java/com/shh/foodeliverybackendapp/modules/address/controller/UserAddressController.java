package com.shh.foodeliverybackendapp.modules.address.controller;

import com.shh.foodeliverybackendapp.modules.address.dto.UserAddressRequest;
import com.shh.foodeliverybackendapp.modules.address.dto.UserAddressResponse;
import com.shh.foodeliverybackendapp.modules.address.service.UserAddressServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Tag(name = "User Addresses", description = "API для управления адресами пользователя")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/user-addresses")
 public class UserAddressController {

    private final UserAddressServiceImpl userAddressService;
    
    public UserAddressController(UserAddressServiceImpl userAddressService) {
        this.userAddressService = userAddressService;
    }
    

    @Operation(summary = "Создать новый адрес пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Адрес успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @PostMapping
    public UserAddressResponse create(@Valid @RequestBody UserAddressRequest request) {
        return userAddressService.create(request);
    }
    


    @Operation(summary = "Получить список адресов текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @GetMapping
    public List<UserAddressResponse> findAll() {
        return userAddressService.findAll();
    }


    @Operation(summary = "Получить адрес по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Адрес найден"),
            @ApiResponse(responseCode = "404", description = "Адрес не найден")})
    @GetMapping("{id}")
    public UserAddressResponse findById(@PathVariable UUID id) {
        return userAddressService.findById(id);
    }


    @Operation(summary = "Обновить адрес пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Адрес успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Адрес не найден")})
    @PutMapping("/{id}")
    public UserAddressResponse update(@PathVariable UUID id, @Valid @RequestBody UserAddressRequest request) {
        return userAddressService.updateById(id, request);
    }
    

    @Operation(summary = "Удалить адрес пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Адрес успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Адрес не найден")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userAddressService.deleteById(id);
    }
}
