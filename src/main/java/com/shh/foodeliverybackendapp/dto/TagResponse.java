package com.shh.foodeliverybackendapp.dto;

import java.util.UUID;

public class TagResponse {
    private UUID id;
    private String name;

    public TagResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {return id;}

    public String getName() {return name;}
}
