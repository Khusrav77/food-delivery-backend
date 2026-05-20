package com.shh.foodeliverybackendapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagRequest {

    @NotBlank
    @Size(max = 20)
    private String tagName;

    public TagRequest() {}

    public TagRequest(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }
}
