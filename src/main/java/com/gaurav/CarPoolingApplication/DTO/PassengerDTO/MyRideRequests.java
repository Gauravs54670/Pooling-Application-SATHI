package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter @Setter
public class MyRideRequests {
    private Long requestId;
    private String driverName;
    private String driverPhoneNumber;
    private String driverProfileUrl;
    private String rideRequestStatus;
    public MyRideRequests(
            Long requestId,
            String driverName,
            String driverPhoneNumber,
            String driverProfileUrl,
            RideRequestStatus rideRequestStatus) {
        this.requestId = requestId;
        this.driverName = driverName;
        this.driverPhoneNumber = driverPhoneNumber;
        this.driverProfileUrl = driverProfileUrl;
        this.rideRequestStatus = rideRequestStatus.name();
    }
}
