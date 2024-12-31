package com.cars.carbookings.controllers;

import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.dto.CarDTO;
import com.cars.carbookings.dto.SearchCarDTO;
import com.cars.carbookings.enums.BidStatus;
import com.cars.carbookings.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/cars")
    public ResponseEntity<List<CarDTO>> getAllCars(){
        return ResponseEntity.ok(adminService.getAllCars());
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable  Long id){
        return ResponseEntity.ok(adminService.getCarById(id));
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id){
        adminService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/car/search")
    public ResponseEntity<List<CarDTO>> searchCar(@RequestBody SearchCarDTO searchCarDTO){
        return ResponseEntity.ok(adminService.searchCar(searchCarDTO));
    }

    @GetMapping("/car/bids")
    public ResponseEntity <List<BidDTO>> getBids(){
        return ResponseEntity.ok(adminService.getBids());
    }

    @PatchMapping("/car/bid/{bidId}/status/{status}")
    public ResponseEntity<String> changeBidStatus(@PathVariable Long bidId, @PathVariable BidStatus status) {
        boolean result = adminService.changeBidStatus(bidId, status);

        if (result) {
            return ResponseEntity.ok("Bid status updated successfully for bid ID: " + bidId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bid with ID " + bidId + " not found.");
        }
    }
}
