package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PassengerRideRequest {
    private String rideCode;
    private Double sourceLat;
    private Double sourceLong;
    private String sourceAddress;
    private Double destinationLat;
    private Double destinationLong;
    private String destinationAddress;
    private Integer requiredSeats;
}
