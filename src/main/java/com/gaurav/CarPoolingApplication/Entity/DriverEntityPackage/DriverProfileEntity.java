package com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage;

import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "driver_entity")
public class DriverProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(nullable = false, unique = true)
    private String driverProfileUrl;
    @Column(nullable = false)
    private String driverProfileCloudId;
    @Column(nullable = false, unique = true)
    private String driverLicenseNumber;
    private LocalDate licenseExpirationDate;
    private String vehicleModel;
    @Column(nullable = false, unique = true)
    private String vehicleNumber;
    @Enumerated(EnumType.STRING)
    private VehicleCategory vehicleCategory;
    @Enumerated(EnumType.STRING)
    private VehicleClass vehicleClass;
    private Integer vehicleSeatCapacity;
    @Enumerated(EnumType.STRING)
    private DriverVerificationStatus driverVerificationStatus;
    @Enumerated(EnumType.STRING)
    private DriverAvailabilityStatus driverAvailabilityStatus;
    private Integer totalCompletedRides;
    private Integer totalCancelledRides;
    private Integer totalDriverRating;
    private Double averageRatingOfDriver;
    private Integer totalReviewCount;
    private LocalDateTime accountCreatedAt;
    private LocalDateTime accountUpdatedAt;
}
