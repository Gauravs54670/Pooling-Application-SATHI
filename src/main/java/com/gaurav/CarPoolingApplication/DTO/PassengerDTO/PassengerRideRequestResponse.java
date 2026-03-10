package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class PassengerRideRequestResponse {
    private Long requestId;
    private String rideCode;
    private String passengerName;
    private String passengerPhoneNumber;
    private String driverName;
    private Integer requestedSeats;
    private String requestStatus;
    private LocalDateTime requestCreatedAt;
    private String sourceAddress;
    private String destinationAddress;
    private LocalDateTime departureTime;
    public PassengerRideRequestResponse(
            Long requestId,
            String rideCode,
            String passengerName,
            String passengerPhoneNumber,
            String driverName,
            Integer requestedSeats,
            RideRequestStatus requestStatus,
            LocalDateTime requestCreatedAt,
            String sourceAddress,
            String destinationAddress,
            LocalDateTime departureTime
    ) {
        this.requestId = requestId;
        this.rideCode = rideCode;
        this.passengerName = passengerName;
        this.passengerPhoneNumber = passengerPhoneNumber;
        this.driverName = driverName;
        this.requestedSeats = requestedSeats;
        this.requestStatus = requestStatus.name();
        this.requestCreatedAt = requestCreatedAt;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.departureTime = departureTime;
    }
}
