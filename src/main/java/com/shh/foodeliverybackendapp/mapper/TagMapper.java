package com.shh.foodeliverybackendapp.mapper;

import com.shh.foodeliverybackendapp.dto.TagRequest;
import com.shh.foodeliverybackendapp.dto.TagResponse;
import com.shh.foodeliverybackendapp.entity.product.Tag;

public class TagMapper {

    public static Tag toEntity(TagRequest request) {
        return new Tag(request.tagName());
    }

    public static TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getLabel());
    }
}
