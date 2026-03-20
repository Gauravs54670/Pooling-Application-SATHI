package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PassengerRideExistRequestDTO {
    private Long rideRequestId;
    private Double actualDestinationLat;
    private Double actualDestinationLong;
    private String actualDestinationAddress;
    private BigDecimal pricePerKm;
}
