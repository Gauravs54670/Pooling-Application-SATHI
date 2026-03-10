package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverAvailabilityStatus;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverVerificationStatus;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@NoArgsConstructor @Getter @Setter @Builder
public class DriverProfileDTO {
    private Long driverId;
    private String email;
    private String phoneNumber;
    private String userFullName;
    private String driverProfileUrl;
    private String driverLicenseNumber;
    private LocalDate licenseExpirationDate;
    private String vehicleModel;
    private String vehicleNumber;
    private Integer vehicleSeatCapacity;
    private VehicleCategory vehicleCategory;
    private VehicleClass vehicleClass;
    private DriverVerificationStatus driverVerificationStatus;
    private DriverAvailabilityStatus driverAvailabilityStatus;
    private Integer totalCompletedRides;
    private Integer totalCancelledRides;
    private Double averageRatingOfDriver;
    private Integer totalReviewCount;
    private LocalDateTime accountCreatedAt;
    public DriverProfileDTO(
            Long driverId,
            String email,
            String phoneNumber,
            String userFullName,
            String driverProfileUrl,
            String driverLicenseNumber,
            LocalDate licenseExpirationDate,
            String vehicleModel,
            String vehicleNumber,
            Integer vehicleSeatCapacity,
            VehicleCategory vehicleCategory,
            VehicleClass vehicleClass,
            DriverVerificationStatus driverVerificationStatus,
            DriverAvailabilityStatus driverAvailabilityStatus,
            Integer totalCompletedRides,
            Integer totalCancelledRides,
            Double averageRatingOfDriver,
            Integer totalReviewCount,
            LocalDateTime accountCreatedAt) {
        this.driverId = driverId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userFullName = userFullName;
        this.driverProfileUrl = driverProfileUrl;
        this.driverLicenseNumber = driverLicenseNumber;
        this.licenseExpirationDate = licenseExpirationDate;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.vehicleSeatCapacity = vehicleSeatCapacity;
        this.vehicleCategory = vehicleCategory;
        this.vehicleClass = vehicleClass;
        this.driverVerificationStatus = driverVerificationStatus;
        this.driverAvailabilityStatus = driverAvailabilityStatus;
        this.totalCompletedRides = totalCompletedRides;
        this.totalCancelledRides = totalCancelledRides;
        this.averageRatingOfDriver = averageRatingOfDriver;
        this.totalReviewCount = totalReviewCount;
        this.accountCreatedAt = accountCreatedAt;
    }
}
