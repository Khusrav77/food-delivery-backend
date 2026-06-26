package com.shh.foodeliverybackendapp.modules.menu.dto.response;

import java.util.UUID;

public record TagResponse(

        UUID id,
        String name,
        String color

) {}