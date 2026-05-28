package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.TagRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.TagResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.Tag;

public class TagMapper {

    public static Tag toEntity(TagRequest request) {

        return new Tag(request.name(), request.color());
    }

    public static TagResponse toResponse(Tag tag) {

        return new TagResponse(tag.getId(), tag.getLabel(), tag.getColor());
    }
}
