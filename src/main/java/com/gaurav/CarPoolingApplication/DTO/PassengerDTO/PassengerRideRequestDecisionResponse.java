package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class PassengerRideRequestDecisionResponse {
    private Long requestId;
    private String rideOtp;
    private String rideCode;
    private String driverName;
    private String driverPhoneNumber;
    private String rideRequestStatus;
    private LocalDateTime decisionTime;
    public PassengerRideRequestDecisionResponse(
            Long requestId,
            String rideOtp,
            String rideCode,
            String driverName,
            String driverPhoneNumber,
            RideRequestStatus rideRequestStatus,
            LocalDateTime decisionTime) {
        this.requestId = requestId;
        this.rideOtp = rideOtp;
        this.rideCode = rideCode;
        this.driverName = driverName;
        this.driverPhoneNumber = driverPhoneNumber;
        this.rideRequestStatus = rideRequestStatus.name();
        this.decisionTime = decisionTime;
    }
}
