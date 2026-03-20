package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@Getter @Setter
public class DriverRidesHistoryDTO {
    private String rideCode;
    private String boardingAddress;
    private String destinationAddress;
    private BigDecimal estimatedTotalDistance;
    private BigDecimal actualTotalDistance;
    private BigDecimal estimatedTotalFare;
    private BigDecimal actualTotalFare;
    private String rideStatus;
    private LocalDateTime rideDepartureTime;
    private LocalDateTime rideCompletedAt;
    public DriverRidesHistoryDTO(
            String rideCode,
            String boardingAddress,
            String destinationAddress,
            BigDecimal estimatedTotalDistance,
            BigDecimal actualTotalDistance,
            BigDecimal estimatedTotalFare,
            BigDecimal actualTotalFare,
            RideStatus rideStatus,
            LocalDateTime rideDepartureTime,
            LocalDateTime rideCompletedAt) {
        this.rideCode = rideCode;
        this.boardingAddress = boardingAddress;
        this.destinationAddress = destinationAddress;
        this.estimatedTotalDistance = estimatedTotalDistance;
        this.actualTotalDistance = actualTotalDistance;
        this.estimatedTotalFare = estimatedTotalFare;
        this.actualTotalFare = actualTotalFare;
        this.rideStatus = rideStatus.name();
        this.rideDepartureTime = rideDepartureTime;
        this.rideCompletedAt = rideCompletedAt;
    }
}