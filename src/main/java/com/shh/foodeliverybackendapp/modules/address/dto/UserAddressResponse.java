package com.shh.foodeliverybackendapp.modules.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Информация об адресе пользователя")
public record UserAddressResponse(

        @Schema(
                description = "Уникальный идентификатор адреса",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(
                description = "Название адреса",
                example = "Дом")
        String title,

        @Schema(
                description = "Город",
                example = "Москва")
        String city,

        @Schema(
                description = "Улица",
                example = "ул. Ленина")
        String street,

        @Schema(
                description = "Номер дома",
                example = "15")
        String house,

        @Schema(
                description = "Квартира",
                example = "25")
        String apartment,

        @Schema(
                description = "Подъезд",
                example = "2")
        String entrance,

        @Schema(
                description = "Этаж",
                example = "8")
        String floor,

        @Schema(
                description = "Домофон",
                example = "125B")
        String intercom,

        @Schema(
                description = "Комментарий для курьера",
                example = "Позвонить за 5 минут")
        String comment,

        @Schema(
                description = "Широта",
                example = "55.755826")
        BigDecimal latitude,

        @Schema(
                description = "Долгота",
                example = "37.617300")
        BigDecimal longitude,

        @Schema(
                description = "Является ли адрес адресом по умолчанию",
                example = "true")
        Boolean isDefault,

        @Schema(
                description = "Дата создания",
                example = "2026-06-26T12:30:15Z")
        Instant createdAt,

        @Schema(
                description = "Дата последнего обновления",
                example = "2026-06-26T15:45:30Z")
        Instant updatedAt
) { }