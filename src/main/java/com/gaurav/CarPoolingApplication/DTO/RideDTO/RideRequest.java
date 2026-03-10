package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import lombok.*;

import java.time.LocalDateTime;
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class RideRequest {
    private Double sourceLat;
    private Double sourceLong;
    private String sourceAddress;
    private Double destinationLat;
    private Double destinationLong;
    private String destinationAddress;
    private LocalDateTime departureTime;
    private Integer availableSeats;
}
