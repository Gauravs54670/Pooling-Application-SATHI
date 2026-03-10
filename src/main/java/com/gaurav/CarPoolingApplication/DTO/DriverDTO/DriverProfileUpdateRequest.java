package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class DriverProfileUpdateRequest {
    private String driverLicenseNumber;
    private LocalDate licenseExpirationDate;
    private String vehicleModel;
    private String vehicleNumber;
    private Integer vehicleSeatCapacity;
    private String vehicleCategory;
    private String vehicleClass;
}
