package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class RideSearchRequestDTO {
    private Double passengerSourceLat;
    private Double passengerSourceLong;
    private String passengerSourceAddress;
    private Double passengerDestinationLat;
    private Double passengerDestinationLong;
    private String passengerDestinationString;
}
