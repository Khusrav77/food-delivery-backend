package com.shh.foodeliverybackendapp.service;


public interface BasicService <I, O>{

    O create(I request);
    O getById(String id);
    O getAll();
    O updateById(String id, I request);
    O deleteById(String id);


}
