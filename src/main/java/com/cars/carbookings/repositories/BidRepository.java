package com.cars.carbookings.repositories;

import com.cars.carbookings.dto.BidDTO;
import com.cars.carbookings.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("SELECT b FROM Bid b JOIN FETCH b.user JOIN FETCH b.car c JOIN FETCH c.user WHERE b.user.id = :userId")
    List<Bid> findAllByUserId(@Param("userId") Long userId);

    List<Bid> findAllByCarId(Long carId);
}
