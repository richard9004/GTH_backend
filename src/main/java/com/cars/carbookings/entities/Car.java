package com.cars.carbookings.entities;

import com.cars.carbookings.dto.CarDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String name;

    private String brand;

    private String type;

    private String transmission;

    private String color;

    private LocalDate year;

    private Boolean sold;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] img;

    private long price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public CarDTO getCarDTO(){
        CarDTO carDTO = new CarDTO();
        carDTO.setId(id);
        carDTO.setName(name);
        carDTO.setBrand(brand);
        carDTO.setType(type);
        carDTO.setDescription(description);
        carDTO.setTransmission(transmission);
        carDTO.setColor(color);
        carDTO.setYear(year);
        carDTO.setSold(sold);
        carDTO.setPrice(price);
        carDTO.setReturnImg(img);
        return carDTO;
    }
}
