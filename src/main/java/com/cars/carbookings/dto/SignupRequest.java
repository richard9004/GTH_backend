package com.cars.carbookings.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String firstName;          // New field
    private String lastName;           // New field
    private String preferredName;      // New field
    private String phone;              // New field
    private String streetAddress;      // New field
    private String suburb;             // New field
    private String city;               // New field
    private String postcode;           // New field
    private String country;            // New field
    private String birthDate;          // New field
    private MultipartFile profileImage; // Change to MultipartFile
    private boolean termsAccepted;     // New field
}