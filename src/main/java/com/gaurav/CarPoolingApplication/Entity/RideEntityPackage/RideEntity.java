package com.gaurav.CarPoolingApplication.Entity.RideEntityPackage;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverProfileEntity;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "ride_entity")
public class RideEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverProfileEntity driverProfileEntity;
    @Column(nullable = false)
    private Double sourceLat;
    @Column(nullable = false)
    private Double sourceLong;
    @Column(nullable = false)
    private String sourceAddress;
    @Column(nullable = false)
    private Double destinationLat;
    @Column(nullable = false)
    private Double destinationLong;
    @Column(nullable = false)
    private String destinationAddress;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleClass vehicleClass;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleCategory vehicleCategory;
    @Column(nullable = false)
    private BigDecimal estimatedTotalDistance;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerKm;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedTotalFare;
    @Column(nullable = false)
    private BigDecimal actualTotalDistance;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal actualTotalFare;
    @Column(nullable = false)
    private Integer totalSeats;
    @Column(nullable = false)
    private Integer availableSeats;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;
    @Column(nullable = false)
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime rideCreatedAt;
    private LocalDateTime rideUpdatedAt;
    private LocalDateTime rideCompletedAt;
    private boolean isRideDeleted = false;
    @Column(nullable = false,unique = true)
    private String rideCode;
    private String actualRoutePath;
}
