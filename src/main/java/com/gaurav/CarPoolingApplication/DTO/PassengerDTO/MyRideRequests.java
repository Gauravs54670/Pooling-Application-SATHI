package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter @Setter
public class MyRideRequests {
    private String rideCode;
    private String rideRequestDecision;
    public MyRideRequests(String rideCode, RideRequestStatus rideRequestStatus) {
        this.rideCode = rideCode;
        this.rideRequestDecision = rideRequestStatus.name();
    }
}
