package com.cars.carbookings.services.customer;

import com.cars.carbookings.dto.AnalyticsDTO;
import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.dto.CarDTO;
import com.cars.carbookings.dto.SearchCarDTO;
import com.cars.carbookings.entities.Bid;
import com.cars.carbookings.entities.Car;
import com.cars.carbookings.entities.User;
import com.cars.carbookings.enums.BidStatus;
import com.cars.carbookings.repositories.BidRepository;
import com.cars.carbookings.repositories.CarRepository;
import com.cars.carbookings.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    private final BidRepository bidRepository;

    @Override
    public boolean createCar(CarDTO carDTO) throws IOException {
//        Optional <User> optionalUser = userRepository.findById(carDTO.getId());
        // Find the user by userId (from the CarDTO)
        Optional<User> optionalUser = userRepository.findById(carDTO.getUserId());

        if(optionalUser.isPresent()){
            // User exists, create a new Car entity and associate it with the user
            User user = optionalUser.get(); // Get the User object

            Car car = new Car();
            car.setName(carDTO.getName());
            car.setBrand(carDTO.getBrand());
            car.setPrice(carDTO.getPrice());
            car.setDescription(carDTO.getDescription());
            car.setTransmission(carDTO.getDescription());
            car.setSold(false);
            car.setColor(carDTO.getColor());
            car.setYear(carDTO.getYear());
            car.setImg(carDTO.getImg().getBytes());
            car.setUser(user);
            carRepository.save(car);
            return true;
        }

        return false;
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
    public boolean updateCar(Long id, CarDTO carDTO) throws IOException {
        Optional <Car> optionalCar = carRepository.findById(id);

        if(optionalCar.isPresent()){
            Car car = optionalCar.get();
            car.setName(carDTO.getName());
            car.setBrand(carDTO.getBrand());
            car.setPrice(carDTO.getPrice());
            car.setDescription(carDTO.getDescription());
            car.setTransmission(carDTO.getDescription());
            car.setColor(carDTO.getColor());
            car.setYear(carDTO.getYear());
            if(carDTO.getImg()!=null)car.setImg(carDTO.getImg().getBytes());

            carRepository.save(car);
            return true;
        }
        return false;
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

    // Method to get cars based on user_id
    public List<CarDTO> getCarsByUserId(Long userId) {
        List<Car> cars = carRepository.findCarsByUserId(userId);
        return cars.stream()
                .map(Car::getCarDTO) // Assuming you have a method to convert Car to CarDTO
                .collect(Collectors.toList());
    }

    @Override
    public boolean bidACar(BidDTO bidDTO) {
        Optional<Car> optionalCar = carRepository.findById(bidDTO.getCarId());
        Optional<User> optionalUser = userRepository.findById(bidDTO.getUserId());

        if(optionalCar.isPresent() && optionalUser.isPresent()){
            Bid bid = new Bid();

            bid.setUser(optionalUser.get());
            bid.setCar(optionalCar.get());
            bid.setBidStatus(BidStatus.PENDING);
            bid.setPrice(bidDTO.getPrice());

            bidRepository.save(bid);
            return true;
        }

        return false;
    }

    @Override
    public List<BidDTO> getBidByUserId(Long userId) {
        return bidRepository.findAllByUserId(userId).stream()
                .map(Bid::getBidDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BidDTO> getBidByCarId(Long carId) {
        return bidRepository.findAllByCarId(carId).stream()
                .map(Bid::getBidDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean changeBidStatus(Long bidId, BidStatus newStatus) {
        // Find the Bid by ID
        Optional<Bid> optionalBid = bidRepository.findById(bidId);

        if (optionalBid.isPresent()) {
            // Update the BidStatus
            Bid bid = optionalBid.get();

            if(bid.getCar().getSold()){
                return  false;
            }

            bid.setBidStatus(newStatus);

            // Save the updated Bid
            bidRepository.save(bid);
            return true;
        }

        // Return false if the Bid was not found
        return false;
    }

    @Override
    public AnalyticsDTO getAnalytics(Long userId) {
        AnalyticsDTO analyticsDTO = new AnalyticsDTO();
        analyticsDTO.setTotalCars(carRepository.countByUserId(userId));
        analyticsDTO.setSoldCars(carRepository.countByUserIdAndSoldTrue(userId));
        return analyticsDTO;
    }


}
