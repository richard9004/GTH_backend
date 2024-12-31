package com.cars.carbookings.entities;

import jakarta.persistence.*;


import lombok.Data;

import java.util.Map;

@Data
@Entity
@Table(name = "organizers")
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Organization Details
    private String orgName;
    private String irdNumber;
    private String orgType;
    private String charityRegistration;
    private String doneeStatus;
    private String preferredOrgName;
    private String overview;

    @Lob
    private String causeAreas; // Stored as a comma-separated string

    private String logo;

    // Registered Address
    private String streetAddress;
    private String suburb;
    private String city;
    private String postalCode;
    private String country;

    // Administrative Contact
    private String adminPosition;
    private String adminSalutation;
    private String adminFirstName;
    private String adminLastName;
    private String adminEmail;
    private String adminPhone;

    // Finance Contact
    private String financeEmail;
    private String financeFirstName;
    private String financeLastName;
    private String financePhone;
    private String financePosition;

    // Payment Details
    private String paymentAccountNumber;
    private String paymentBankName;
    private String paymentBankStatement; // File path or identifier for the uploaded statement

    // Settlement Details
    private String settlementAccountNumber;
    private String settlementBankName;
    private String settlementBankStatement; // File path or identifier for the uploaded statement

    private boolean sameAsAbove;
    private boolean terms;
}
