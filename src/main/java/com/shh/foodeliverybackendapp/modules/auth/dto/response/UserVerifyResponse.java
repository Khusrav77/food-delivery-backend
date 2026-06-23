package com.shh.foodeliverybackendapp.modules.auth.dto.response;
import com.shh.foodeliverybackendapp.modules.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Basic information about the authenticated user")
public record UserVerifyResponse(

        @Schema(
                description = "User unique identifier",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(
                description = "User phone number",
                example = "79991234567")
        String phone,

        @Schema(
                description = "User role",
                example = "USER")
        Role role

) { }