package com.cars.carbookings.services.auth;

import com.cars.carbookings.dto.SignupRequest;
import com.cars.carbookings.dto.UserDTO;

import java.io.IOException;

public interface AuthService {
    UserDTO signup(SignupRequest signupRequest) throws IOException;
    Boolean hasUserWithEmail(String email);

}
