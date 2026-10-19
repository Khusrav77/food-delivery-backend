package com.marketplace.identity.application.port.in;

public interface UseCase <I, R>{
    R execute(I input);
}
