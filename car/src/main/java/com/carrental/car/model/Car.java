package com.carrental.car.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name="cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(length=32)
    private String vin;

    @Column(length=32)
    private String licensePlate;

    @Column(nullable=false, length=40)
    private String make;
    @Column(nullable = false, length = 60)
    private String model;

    private Integer year;
    private String trim;
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private FuelType fuelType;

    private Integer odometerMile;
    private Short seats;
    private short doors;
    private String features;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;



    /* === Getters and Setters === **/
    public Long getId(){return id;}
    public Long getOwnerId(){return ownerId;}
    public String getVin(){return vin;}
    public String getLicensePlate(){return licensePlate;}
    public String getMake(){return make;}
    public String getModel(){return model;}
    public Integer getYear(){return year;}
    public String getTrim(){return trim;}
    public String getColor(){return color;}
    public Transmission getTransmission(){return transmission;}
    public FuelType getFuelType(){return fuelType;}
    public Integer getOdometerMile(){return odometerMile;}
    public Short getSeats(){return seats;}
    public Short getDoors(){return doors;}
    public String getFeatures(){return features;}
    public Instant getCreatedAt(){return createdAt;}
    public Instant getUpdatedAt(){return updatedAt;}


    public void setId(Long id){this.id=id;}
    public void setOwnerId(Long ownerId){this.ownerId=ownerId;}
    public void setVin(String vin){this.vin=vin;}
    public void setLicensePlate(String licensePlate){this.licensePlate=licensePlate;}
    public void setMake(String make){this.make=make;}
    public void setModel(String model){this.model=model;}
    public void setYear(Integer year){this.year=year;}
    public void setTrim(String trim){this.trim=trim;}
    public void setColor(String color){this.color=color;}
    public void setTransmission(Transmission transmission){this.transmission=transmission;}
    public void setFuelType(FuelType fuelType){this.fuelType=fuelType;}
    public void setOdometerMile(Integer odometerMile){this.odometerMile=odometerMile;}
    public void setSeats(Short seats){this.seats=seats;}
    public void setDoors(Short doors){this.doors=doors;}
    public void setFeatures(String features){this.features=features;}
    public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}
