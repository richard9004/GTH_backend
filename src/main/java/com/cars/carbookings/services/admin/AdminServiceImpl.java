package com.cars.carbookings.services.admin;

import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.dto.CarDTO;
import com.cars.carbookings.dto.SearchCarDTO;
import com.cars.carbookings.entities.Bid;
import com.cars.carbookings.entities.Car;
import com.cars.carbookings.enums.BidStatus;
import com.cars.carbookings.repositories.BidRepository;
import com.cars.carbookings.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final CarRepository carRepository;

    private final BidRepository bidRepository;

    @Override
    public List<BidDTO> getBids() {
        return bidRepository.findAll().stream()
                .map(Bid::getBidDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(Car::getCarDTO) // Use the getCarDTO method to map entity to DTO
                .collect(Collectors.toList());
    }

    @Override
    public CarDTO getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDTO).orElse(null);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<CarDTO> searchCar(SearchCarDTO searchCarDTO) {

        if ("".equals(searchCarDTO.getBrand())) {
            searchCarDTO.setBrand(null);
        }
        if ("".equals(searchCarDTO.getType())) {
            searchCarDTO.setType(null);
        }
        if ("".equals(searchCarDTO.getColor())) {
            searchCarDTO.setColor(null);
        }
        if ("".equals(searchCarDTO.getTransmission())) {
            searchCarDTO.setTransmission(null);
        }


        // Check if the search criteria are empty first
        if (isEmpty(searchCarDTO.getBrand()) && isEmpty(searchCarDTO.getType())
                && isEmpty(searchCarDTO.getColor()) && isEmpty(searchCarDTO.getTransmission())) {
            return carRepository.findAll().stream()
                    .map(Car::getCarDTO)
                    .collect(Collectors.toList());
        }



        // Query only if there are search criteria
        List<Car> cars = carRepository.findCars(
                searchCarDTO.getBrand(),
                searchCarDTO.getType(),
                searchCarDTO.getColor(),
                searchCarDTO.getTransmission());

        return cars.stream()
                .map(Car::getCarDTO)
                .collect(Collectors.toList());
    }


    @Override
    public boolean changeBidStatus(Long bidId, BidStatus newStatus) {
        // Find the Bid by ID
        Optional<Bid> optionalBid = bidRepository.findById(bidId);

        if (optionalBid.isPresent()) {
            // Update the BidStatus
            Bid bid = optionalBid.get();
            bid.setBidStatus(newStatus);

            // Save the updated Bid
            bidRepository.save(bid);
            return true;
        }

        // Return false if the Bid was not found
        return false;
    }

}
