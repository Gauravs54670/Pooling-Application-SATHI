package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class DriverRideRequestDecisionResponse {
    private Long requestId;
    private String passengerName;
    private String passengerPhoneNumber;
    private Integer approvedSeats;
    private String requestStatus;
    private String passengerPickUpAddress;
    private String passengerDropAddress;
    private LocalDateTime decisionTime;
}
