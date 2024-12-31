package com.cars.carbookings.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CarDTO {
    private Long id;

    private String name;

    private String brand;

    private String type;

    private String transmission;

    private String color;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate year;

    private Boolean sold;

    private String description;

    private MultipartFile img;

    private Long userId;

    private byte[] returnImg;

    private long price;
}
