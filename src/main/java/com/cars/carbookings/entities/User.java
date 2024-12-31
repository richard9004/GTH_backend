package com.cars.carbookings.entities;

import com.cars.carbookings.dto.UserDTO;
import com.cars.carbookings.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "\"user\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String preferredName;
    private String email;
    private String password;
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

    public UserDTO getUserDTO(){
        UserDTO userDTO =  new UserDTO();
        userDTO.setId(id);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setPreferredName(preferredName);
        userDTO.setEmail(email);
        userDTO.setUserRole(userRole);
        userDTO.setPhone(phone);
        userDTO.setStreetAddress(streetAddress);
        userDTO.setSuburb(suburb);
        userDTO.setCity(city);
        userDTO.setPostcode(postcode);
        userDTO.setCountry(country);
        userDTO.setBirthDate(birthDate);
        userDTO.setProfileImage(profileImage);
        userDTO.setTermsAccepted(termsAccepted);
        return userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}