package com.cars.carbookings.dto;

import com.cars.carbookings.enums.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String preferredName;
    private String email;
    private UserRole userRole;
    private String phone;
    private String streetAddress;
    private String suburb;
    private String city;
    private String postcode;
    private String country;
    private String birthDate;
    private String profileImage;
    private boolean termsAccepted;

}
