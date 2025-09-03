package com.carrental.car.doa;

import com.carrental.car.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwnerId(Long ownerId);

    @Query("select c.ownerId from Car c where c.id = :carId")
    Optional<Long> findOwnerIdByCarId(@Param("carId") Long carId);

}
