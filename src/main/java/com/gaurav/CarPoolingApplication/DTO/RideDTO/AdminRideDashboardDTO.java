package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@Getter @Setter
public class AdminRideDashboardDTO {
    private Long rideId;
    private String rideCode;
    private String boardingAddress;
    private String destinationAddress;
    private String rideStatus;
    private BigDecimal estimatedTotalFare;
    private BigDecimal actualTotalFare;
    private BigDecimal estimatedTotalDistance;
    private BigDecimal actualTotalDistance;
    private Integer totalPassengerTravelledInRide;
    private LocalDateTime departureTime;
    private LocalDateTime rideCompletedAt;
    public AdminRideDashboardDTO(
            Long rideId,
            String rideCode,
            String sourceAddress,
            String destinationAddress,
            RideStatus rideStatus,
            BigDecimal estimatedTotalFare,
            BigDecimal actualTotalFare,
            BigDecimal estimatedTotalDistance,
            BigDecimal actualTotalDistance,
            Integer totalPassengerTravelledInRide,
            LocalDateTime departureTime,
            LocalDateTime rideCompletedAt) {
        this.rideId = rideId;
        this.rideCode = rideCode;
        this.boardingAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.rideStatus = rideStatus.name();
        this.estimatedTotalFare = estimatedTotalFare;
        this.actualTotalFare = actualTotalFare;
        this.estimatedTotalDistance = estimatedTotalDistance;
        this.actualTotalDistance = actualTotalDistance;
        this.totalPassengerTravelledInRide = totalPassengerTravelledInRide;
        this.departureTime = departureTime;
        this.rideCompletedAt = rideCompletedAt;
    }
}
