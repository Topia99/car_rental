package com.carrental.car.dto;

import com.carrental.car.model.FuelType;
import com.carrental.car.model.Transmission;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateCarRequest(
        String color,
        Transmission transmission,
        FuelType fuelType,
        @Min(0) Integer odometerMile,
        @Min(1) @Max(9) Short seats,
        @Min(1) @Max(6) Short doors,
        String features
) {}
