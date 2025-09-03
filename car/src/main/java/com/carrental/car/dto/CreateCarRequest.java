package com.carrental.car.dto;

import com.carrental.car.model.FuelType;
import com.carrental.car.model.Transmission;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCarRequest(
        @NotBlank String make,
        @NotBlank String model,
        @Min(1980) @Max(2100) Integer year,
        String trim,
        String color,
        Transmission transmission,
        FuelType fuelType,
        String vin,
        String licensePlate,
        @Min(0) Integer odometerMile,
        @Min(1) @Max(9) Short seats,
        @Min(1) @Max(6) Short doors,
        String features
) { }

