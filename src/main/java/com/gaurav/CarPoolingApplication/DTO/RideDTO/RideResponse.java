package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@Builder @Data
public class RideResponse {
    private String rideCode;
    private String driverName;
    private Double sourceLat;
    private Double sourceLong;
    private String sourceAddress;
    private Double destinationLat;
    private Double destinationLong;
    private String destinationAddress;
    private LocalDateTime departureTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal estimatedTotalDistance;
    private BigDecimal pricePerKm;
    private BigDecimal estimatedTotalFare;
    private BigDecimal actualTotalDistance;
    private BigDecimal actualTotalFare;
    private LocalDateTime rideCreatedAt;
    public RideResponse(
            String rideCode,
            String driverName,
            Double sourceLat,
            Double sourceLong,
            String sourceAddress,
            Double destinationLat,
            Double destinationLong,
            String destinationAddress,
            LocalDateTime departureTime,
            Integer totalSeats,
            Integer availableSeats,
            BigDecimal estimatedTotalDistance,
            BigDecimal pricePerKm,
            BigDecimal estimatedTotalFare,
            BigDecimal actualTotalDistance,
            BigDecimal actualTotalFare,
            LocalDateTime rideCreatedAt){
        this.rideCode = rideCode;
        this.driverName = driverName;
        this.sourceLat = sourceLat;
        this.sourceLong = sourceLong;
        this.sourceAddress = sourceAddress;
        this.destinationLat = destinationLat;
        this.destinationLong = destinationLong;
        this.destinationAddress = destinationAddress;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.estimatedTotalDistance = estimatedTotalDistance;
        this.pricePerKm = pricePerKm;
        this.estimatedTotalFare = estimatedTotalFare;
        this.actualTotalDistance = actualTotalDistance;
        this.actualTotalFare = actualTotalFare;
        this.rideCreatedAt = rideCreatedAt;
    }
}
