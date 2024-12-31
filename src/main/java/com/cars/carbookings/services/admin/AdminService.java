package com.cars.carbookings.services.admin;

import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.dto.CarDTO;
import com.cars.carbookings.dto.SearchCarDTO;
import com.cars.carbookings.enums.BidStatus;

import java.util.List;

public interface AdminService {
    List<CarDTO> getAllCars();

    CarDTO getCarById(Long id);

    void deleteCar(Long id);

    List<CarDTO> searchCar(SearchCarDTO searchCarDTO);

    List<BidDTO> getBids();

    boolean changeBidStatus(Long bidId, BidStatus newStatus);

}
