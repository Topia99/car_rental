package com.carrental.car.dto;

import com.carrental.car.model.FuelType;
import com.carrental.car.model.Transmission;

public record CarResponse(
        Long id,
        Long ownerId,
        String make,
        String model,
        Integer year,
        String color,
        Transmission transmission,
        FuelType fuelType,
        String features
) {
}
