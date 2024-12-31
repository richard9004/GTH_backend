package com.cars.carbookings.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class OrganizerRequest {

    private String orgName;
    private String irdNumber;
    private String orgType;
    private String charityRegistration;
    private String doneeStatus;
    private String preferredOrgName;
    private String overview;
    private String causeAreas;  // This will be a String now
    private MultipartFile logo;
    private String streetAddress;
    private String suburb;
    private String city;
    private String postalCode;
    private String country;
    private String adminPosition;
    private String adminSalutation;
    private String adminFirstName;
    private String adminLastName;
    private String adminEmail;
    private String adminPhone;
    private String financeEmail;
    private String financeFirstName;
    private String financeLastName;
    private String financePhone;
    private String financePosition;
    private String paymentAccountNumber;
    private String paymentBankName;
    private MultipartFile paymentBankStatement;
    private String settlementAccountNumber;
    private String settlementBankName;
    private MultipartFile settlementBankStatement;
    private boolean sameAsAbove;
    private boolean terms;
}