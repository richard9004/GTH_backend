package com.cars.carbookings.dto;

import com.cars.carbookings.enums.BidStatus;
import lombok.Data;

@Data
public class BidDTO {
    private Long id;

    private Long price;

    private BidStatus bidStatus;

    private Long carId;

    private Long userId;

    private String username;

    private String carName;

    private String carBrand;

    private String email;

    private String sellerName;
}