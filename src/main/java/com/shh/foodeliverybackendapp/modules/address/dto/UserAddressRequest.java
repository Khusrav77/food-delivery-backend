package com.shh.foodeliverybackendapp.modules.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Запрос на создание или обновление адреса пользователя")
public record UserAddressRequest(

        @Schema(
                description = "Название адреса",
                example = "Дом")
        @Size(max = 50)
        String title,

        @Schema(
                description = "Город",
                example = "Москва",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String city,

        @Schema(
                description = "Улица",
                example = "ул. Ленина",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 150)
        String street,

        @Schema(
                description = "Номер дома",
                example = "15",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 20)
        String house,

        @Schema(
                description = "Квартира",
                example = "25")
        @Size(max = 20)
        String apartment,

        @Schema(
                description = "Подъезд",
                example = "2")
        @Size(max = 20)
        String entrance,

        @Schema(
                description = "Этаж",
                example = "8")
        @Size(max = 10)
        String floor,

        @Schema(
                description = "Домофон",
                example = "125B")
        @Size(max = 20)
        String intercom,

        @Schema(
                description = "Комментарий для курьера",
                example = "Позвонить за 5 минут до приезда")
        @Size(max = 255)
        String comment,

        @Schema(
                description = "Широта",
                example = "55.755826")
        @DecimalMin("-90.000000")
        @DecimalMax("90.000000")
        BigDecimal latitude,

        @Schema(
                description = "Долгота",
                example = "37.617300")
        @DecimalMin("-180.000000")
        @DecimalMax("180.000000")
        BigDecimal longitude,

        @Schema(
                description = "Является ли адрес адресом по умолчанию",
                example = "true")
        Boolean isDefault
) { }