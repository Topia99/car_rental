package com.carrental.car.service;

import com.carrental.car.doa.CarRepository;
import com.carrental.car.dto.CarResponse;
import com.carrental.car.dto.CreateCarRequest;
import com.carrental.car.dto.UpdateCarRequest;
import com.carrental.car.model.Car;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CarService {
    private final CarRepository cars;

    public CarService(CarRepository cars) {
        this.cars = cars;
    }

    public CarResponse create(Long ownerId, CreateCarRequest r){
        Car c = new Car();
        c.setOwnerId(ownerId);
        c.setMake(r.make());
        c.setModel(r.model());
        c.setYear(r.year());
        c.setTrim(r.trim());
        c.setColor(r.color());
        c.setTransmission(r.transmission());
        c.setFuelType(r.fuelType());
        c.setVin(r.vin());
        c.setLicensePlate(r.licensePlate());
        c.setOdometerMile(r.odometerMile());
        c.setSeats(r.seats());
        c.setDoors(r.doors());
        c.setFeatures(r.features());

        return toResp(cars.save(c));
    }

    public List<CarResponse> myCars(Long ownerId){
        return cars.findByOwnerId(ownerId)
                .stream()
                .map(this::toResp)
                .toList();
    }

    public CarResponse get(Long id, Long requester){
        Car c = cars.findById(id).orElseThrow(() -> nf("car"));
        if(!c.getOwnerId().equals(requester)) throw fb();

        return toResp(c);
    }

    public CarResponse update(Long id, Long requester, UpdateCarRequest r){
        Car c = cars.findById(id).orElseThrow(() -> nf("car"));

        if(!c.getOwnerId().equals(requester)) throw fb();

        if (r.color()!=null) c.setColor(r.color());
        if (r.transmission()!=null) c.setTransmission(r.transmission());
        if (r.fuelType()!=null) c.setFuelType(r.fuelType());
        if (r.odometerMile()!=null) c.setOdometerMile(r.odometerMile());
        if (r.seats()!=null) c.setSeats(r.seats());
        if (r.doors()!=null) c.setDoors(r.doors());
        if (r.features()!=null) c.setFeatures(r.features());
        return toResp(c);
    }

    public void delete(Long id, Long requester){
        Car c = cars.findById(id).orElseThrow(() -> nf("car"));
        if (!c.getOwnerId().equals(requester)) throw fb();
        cars.delete(c);
    }


    /* -- Helper methods -- */
    private CarResponse toResp(Car c){
        return new CarResponse(c.getId(), c.getOwnerId(), c.getMake(), c.getModel(),
                c.getYear(), c.getColor(), c.getTransmission(), c.getFuelType(), c.getFeatures());
    }

    private ResponseStatusException nf(String w){ return new ResponseStatusException(HttpStatus.NOT_FOUND, w+" not found"); }
    private ResponseStatusException fb(){ return new ResponseStatusException(HttpStatus.FORBIDDEN, "not owner"); }
}
