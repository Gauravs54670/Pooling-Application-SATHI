package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class RideCompleteResponse {
    private String rideCode;
    private String sourceAddress;
    private String destinationAddress;
    private LocalDateTime departureTime;
    private LocalDateTime rideCompletedAt;
    private BigDecimal estimatedTotalDistance;
    private BigDecimal actualTotalDistance;
    private BigDecimal estimatedTotalFare;
    private BigDecimal actualTotalFare;
    private Integer totalSeats;
    private Integer availableSeats;
}
