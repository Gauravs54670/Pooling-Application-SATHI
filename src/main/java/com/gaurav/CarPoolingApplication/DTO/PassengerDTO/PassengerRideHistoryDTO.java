package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter @Setter
public class PassengerRideHistoryDTO {
    private Long requestId;
    private String rideCode;
    private String sourceAddress;
    private String destinationAddress;
    private BigDecimal actualTotalFare;
    private BigDecimal actualTotalDistance;
    private Integer requestedSeats;
    private String vehicleClass;
    private String vehicleCategory;
    private String vehicleModel;
    private LocalDateTime rideRequestedAt;
    private LocalDateTime rideDepartureTime;
    private LocalDateTime rideCompletedAt;
    private String driverFullName;
    private String driverProfileUrl;
    private Double averageRatingOfDriver;
    public PassengerRideHistoryDTO(
            Long requestId,
            String rideCode,
            String sourceAddress,
            String destinationAddress,
            BigDecimal actualTotalFare,
            BigDecimal actualTotalDistance,
            Integer requestedSeats,
            VehicleClass vehicleClass,
            VehicleCategory vehicleCategory,
            String vehicleModel,
            LocalDateTime rideRequestedAt,
            LocalDateTime rideDepartureTime,
            LocalDateTime rideCompletedAt,
            String driverFullName,
            String driverProfileUrl,
            Double averageRatingOfDriver) {
        this.requestId = requestId;
        this.rideCode = rideCode;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.actualTotalFare = actualTotalFare;
        this.actualTotalDistance = actualTotalDistance;
        this.requestedSeats = requestedSeats;
        this.vehicleClass = vehicleClass.name();
        this.vehicleCategory = vehicleCategory.name();
        this.vehicleModel = vehicleModel;
        this.rideRequestedAt = rideRequestedAt;
        this.rideDepartureTime = rideDepartureTime;
        this.rideCompletedAt = rideCompletedAt;
        this.driverFullName = driverFullName;
        this.driverProfileUrl = driverProfileUrl;
        this.averageRatingOfDriver = averageRatingOfDriver;
    }
}
