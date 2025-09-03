package com.carrental.car.controller;


import com.carrental.car.dto.CarResponse;
import com.carrental.car.dto.CreateCarRequest;
import com.carrental.car.service.CarService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService service;

    public CarController(CarService service){
        this.service = service;
    }

    private Long uid(Authentication auth){
        Long id = AuthUtil.uid(auth);
    }

    @PostMapping("/me")
    public CarResponse create(@Valid @RequestBody CreateCarRequest req){
        return service.create(, req);
    }

    @GetMapping("/me")
    public List<CarResponse> myCars(@AuthenticationPrincipal AccountPrincipal me){
        return service.myCars(me.getId());
    }


}
