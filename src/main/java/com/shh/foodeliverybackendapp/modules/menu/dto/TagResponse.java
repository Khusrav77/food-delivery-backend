package com.shh.foodeliverybackendapp.modules.menu.dto;

import java.util.UUID;

public record TagResponse(

        UUID id,
        String name,
        String color

) {}