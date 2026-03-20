package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class PassengerExitRideResponseDTO {
    private Long requestId;
    private String rideCode;
    private String boardingAddress;
    private String exitAddress;
    private BigDecimal pricePerKm;
    private BigDecimal actualDistance;
    private BigDecimal actualFare;
    private Integer seatsReleased;
    private LocalDateTime exitedAt;
}
