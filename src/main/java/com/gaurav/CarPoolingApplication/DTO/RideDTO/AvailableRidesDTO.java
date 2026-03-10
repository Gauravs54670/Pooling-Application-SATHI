package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor @Getter @Setter
public class AvailableRidesDTO {
    private String rideCode;
    private String driverName;
    private Double driverRating;
    private String driverSourceLocation;
    private String driverDestinationLocation;
    private LocalDateTime rideDepartureTime;
    private Integer totalSeats;
    private Integer totalAvailableSeats;
    private BigDecimal pricePerKm;
    private String vehicleModel;
    private String vehicleCategory;
    private String vehicleClass;
    private String rideStatus;

    public AvailableRidesDTO(
            String rideCode,
            String driverName,
            Double driverRating,
            String driverSourceLocation,
            String driverDestinationLocation,
            LocalDateTime rideDepartureTime,
            Integer totalSeats,
            Integer totalAvailableSeats,
            BigDecimal pricePerKm,
            String vehicleModel,
            VehicleCategory vehicleCategory,
            VehicleClass vehicleClass,
            RideStatus rideStatus) {
        this.rideCode = rideCode;
        this.driverName = driverName;
        this.driverRating = driverRating;
        this.driverSourceLocation = driverSourceLocation;
        this.driverDestinationLocation = driverDestinationLocation;
        this.rideDepartureTime = rideDepartureTime;
        this.totalSeats = totalSeats;
        this.totalAvailableSeats = totalAvailableSeats;
        this.pricePerKm = pricePerKm;
        this.vehicleModel = vehicleModel;
        this.vehicleCategory = vehicleCategory.name();
        this.vehicleClass = vehicleClass.name();
        this.rideStatus = rideStatus.name();
    }
}
