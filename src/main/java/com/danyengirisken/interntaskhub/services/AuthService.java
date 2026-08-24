package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;
import com.danyengirisken.interntaskhub.entity.dto.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);
}