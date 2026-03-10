package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class DriverProfileRequest {
    private String driverLicenseNumber;
    private LocalDate licenseExpirationDate;
    private String vehicleModel;
    private String vehicleNumber;
    private String vehicleCategory;
    private String vehicleClass;
    private Integer vehicleSeatCapacity;
}
