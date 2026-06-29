package com.shh.foodeliverybackendapp.modules.base;

public interface AbstractMapper <E, I, O> {
    O toDto(E entity, O dto);
    E toEntity(E entity, I dto);
    O updateDto(E entity, I dto);
}
