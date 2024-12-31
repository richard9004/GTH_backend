package com.cars.carbookings.repositories;

import com.cars.carbookings.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE " +
            "(:brand IS NULL OR LOWER(c.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:type IS NULL OR LOWER(c.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND " +
            "(:color IS NULL OR LOWER(c.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
            "(:transmission IS NULL OR LOWER(c.transmission) LIKE LOWER(CONCAT('%', :transmission, '%')))")
    List<Car> findCars(@Param("brand") String brand,
                       @Param("type") String type,
                       @Param("color") String color,
                       @Param("transmission") String transmission);


    // Custom query to find cars based on user_id
    @Query("SELECT c FROM Car c WHERE c.user.id = :userId")
    List<Car> findCarsByUserId(@Param("userId") Long userId);

    Long countByUserId(Long userId);

    Long countByUserIdAndSoldTrue(Long userId);
}
