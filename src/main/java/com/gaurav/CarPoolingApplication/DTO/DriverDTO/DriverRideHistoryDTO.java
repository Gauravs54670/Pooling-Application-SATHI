package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleCategory;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.VehicleClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter @Setter
public class DriverRideHistoryDTO {
    private Long rideId;
    private String rideCode;
    private String sourceAddress;
    private String destinationAddress;
    private BigDecimal actualTotalDistance;
    private BigDecimal actualTotalFare;
    private BigDecimal pricePerKm;
    private VehicleClass vehicleClass;
    private VehicleCategory vehicleCategory;
    private Integer totalSeats;
    private Integer availableSeats;
    private LocalDateTime departureTime;
    private LocalDateTime rideCompletedAt;
}
