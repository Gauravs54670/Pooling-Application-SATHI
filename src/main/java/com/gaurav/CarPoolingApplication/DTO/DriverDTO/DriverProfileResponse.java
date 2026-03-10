package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class DriverProfileResponse {
    private String driverProfileUrl;
    private String driverLicenseNumber;
    private LocalDate licenseExpirationDate;
    private String vehicleModel;
    private String vehicleNumber;
    private VehicleCategory vehicleCategory;
    private VehicleClass vehicleClass;
}
