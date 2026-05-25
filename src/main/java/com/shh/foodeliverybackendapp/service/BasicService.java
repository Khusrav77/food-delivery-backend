package com.shh.foodeliverybackendapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BasicService <I, O>{

    O create(I request);
    O findById(UUID id);
    List<O> findAll();
    O updateById(UUID id, I request);
    void deleteById(UUID id);
}
