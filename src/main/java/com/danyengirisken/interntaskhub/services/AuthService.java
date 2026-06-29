package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;

/**
 * Kimlik dogrulama is mantigi (carbon: interface + Impl konvansiyonu).
 */
public interface AuthService {

    LoginResponse login(LoginRequest request);
}
