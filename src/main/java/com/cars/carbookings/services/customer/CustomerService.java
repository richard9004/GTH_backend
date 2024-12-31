package com.cars.carbookings.services.customer;

import com.cars.carbookings.dto.AnalyticsDTO;
import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.dto.CarDTO;
import com.cars.carbookings.dto.SearchCarDTO;
import com.cars.carbookings.enums.BidStatus;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    boolean createCar(CarDTO carDTO) throws IOException;

    List<CarDTO> getAllCars();

    CarDTO getCarById(Long id);

    void deleteCar(Long id);

    boolean updateCar(Long id, CarDTO carDTO) throws IOException;

    List<CarDTO> searchCar(SearchCarDTO searchCarDTO);

    List<CarDTO> getCarsByUserId(Long userId);

    boolean bidACar(BidDTO bidDTO);

    List<BidDTO> getBidByUserId(Long userId);

    List<BidDTO> getBidByCarId(Long carId);

    boolean changeBidStatus(Long bidId, BidStatus newStatus);

    AnalyticsDTO getAnalytics(Long userId);
}
