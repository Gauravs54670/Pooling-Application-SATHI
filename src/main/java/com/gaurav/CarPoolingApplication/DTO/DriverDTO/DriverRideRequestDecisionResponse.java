package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class DriverRideRequestDecisionResponse {
    private Long requestId;
    private String rideCode;
    private String passengerName;
    private String passengerPhoneNumber;
    private Integer approvedSeats;
    private String requestStatus;
    private String rideOTP;
    private LocalDateTime decisionTime;
}
