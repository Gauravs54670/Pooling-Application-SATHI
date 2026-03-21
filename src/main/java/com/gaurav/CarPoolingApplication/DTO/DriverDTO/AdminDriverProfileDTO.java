package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverAvailabilityStatus;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverVerificationStatus;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserAccountStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@NoArgsConstructor
@Getter @Setter
public class AdminDriverProfileDTO {
    private String userFullName;
    private String email;
    private String phoneNumber;
    private String userAccountStatus;
    private String driverProfileUrl;
    private String driverLicenseNumber;
    private LocalDate licenseExpirationDate;
    private Boolean isDriverVerified;
    private Boolean driverPhoneNumberVerificationStatus;
    private String vehicleModel;
    private String vehicleNumber;
    private String vehicleCategory;
    private String vehicleClass;
    private Integer vehicleSeatCapacity;
    private String driverVerificationStatus;
    private String driverAvailabilityStatus;
    private Integer totalCompletedRides;
    private Integer totalCancelledRides;
    private Integer totalDriverRating;
    private Double averageRatingOfDriver;
    private Integer totalReviewCount;
    private LocalDateTime accountCreatedAt;
    private LocalDateTime accountUpdatedAt;
    public AdminDriverProfileDTO(
            String userFullName,
            String email,
            String phoneNumber,
            UserAccountStatus userAccountStatus,
            String driverProfileUrl,
            String driverLicenseNumber,
            LocalDate licenseExpirationDate,
            Boolean isDriverVerified,
            Boolean driverPhoneNumberVerificationStatus,
            String vehicleModel,
            String vehicleNumber,
            VehicleCategory vehicleCategory,
            VehicleClass vehicleClass,
            Integer vehicleSeatCapacity,
            DriverVerificationStatus driverVerificationStatus,
            DriverAvailabilityStatus driverAvailabilityStatus,
            Integer totalCompletedRides,
            Integer totalCancelledRides,
            Integer totalDriverRating,
            Double averageRatingOfDriver,
            Integer totalReviewCount,
            LocalDateTime accountCreatedAt,
            LocalDateTime accountUpdatedAt) {
        this.userFullName = userFullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userAccountStatus = userAccountStatus.name();
        this.driverProfileUrl = driverProfileUrl;
        this.driverLicenseNumber = driverLicenseNumber;
        this.licenseExpirationDate = licenseExpirationDate;
        this.isDriverVerified = isDriverVerified;
        this.driverPhoneNumberVerificationStatus = driverPhoneNumberVerificationStatus;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.vehicleCategory = vehicleCategory.name();
        this.vehicleClass = vehicleClass.name();
        this.vehicleSeatCapacity = vehicleSeatCapacity;
        this.driverVerificationStatus = driverVerificationStatus.name();
        this.driverAvailabilityStatus = driverAvailabilityStatus.name();
        this.totalCompletedRides = totalCompletedRides;
        this.totalCancelledRides = totalCancelledRides;
        this.totalDriverRating = totalDriverRating;
        this.averageRatingOfDriver = averageRatingOfDriver;
        this.totalReviewCount = totalReviewCount;
        this.accountCreatedAt = accountCreatedAt;
        this.accountUpdatedAt = accountUpdatedAt;
    }
}
