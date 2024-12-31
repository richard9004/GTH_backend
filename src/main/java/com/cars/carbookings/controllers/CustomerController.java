package com.cars.carbookings.controllers;

import com.cars.carbookings.dto.AnalyticsDTO;
import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.dto.CarDTO;
import com.cars.carbookings.dto.SearchCarDTO;
import com.cars.carbookings.enums.BidStatus;
import com.cars.carbookings.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/car")
    public ResponseEntity<?> addCar(@ModelAttribute CarDTO carDTO) throws IOException{
        boolean success = customerService.createCar(carDTO);
        if(success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDTO>> getAllCars(){
        return ResponseEntity.ok(customerService.getAllCars());
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable  Long id){
        return ResponseEntity.ok(customerService.getCarById(id));
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id){
        customerService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @ModelAttribute CarDTO carDTO) throws IOException{
        boolean success = customerService.updateCar(id, carDTO);
        if(success) return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @PostMapping("/car/search")
    public ResponseEntity<List<CarDTO>> searchCar(@RequestBody SearchCarDTO searchCarDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // Return a bad request (400) with validation errors
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(customerService.searchCar(searchCarDTO));
    }

    @GetMapping("/my-cars/{userId}")
    public ResponseEntity<List<CarDTO>> getCarsByUserId(@PathVariable Long userId) {
        List<CarDTO> carDTOs = customerService.getCarsByUserId(userId);
        return ResponseEntity.ok(carDTOs);
    }

    @PostMapping("/car/bid")
    public ResponseEntity<?> bidACar(@RequestBody BidDTO bidDTO){
        boolean success = customerService.bidACar(bidDTO);
        if(success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping("/car/bids/{userId}")
    public ResponseEntity<List<BidDTO>> getBidByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getBidByUserId(userId));
    }

    @GetMapping("/car/{carId}/bids")
    public ResponseEntity<List<BidDTO>> getBidByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(customerService.getBidByCarId(carId));
    }

    @PatchMapping("/car/bid/{bidId}/status/{status}")
    public ResponseEntity<String> changeBidStatus(@PathVariable Long bidId, @PathVariable BidStatus status) {
        boolean result = customerService.changeBidStatus(bidId, status);

        if (result) {
            return ResponseEntity.ok("Bid status for Customer updated successfully for bid ID: " + bidId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bid with ID " + bidId + " not found.");
        }
    }

    @GetMapping("/car/analytics/{userId}")
    public ResponseEntity<?> getAnalytics(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getAnalytics(userId));
    }


}
