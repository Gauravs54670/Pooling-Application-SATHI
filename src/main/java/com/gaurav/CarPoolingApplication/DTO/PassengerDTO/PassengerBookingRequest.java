package com.gaurav.CarPoolingApplication.DTO.PassengerDTO;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class PassengerBookingRequest {
    private Long requestId;
    private String rideCode;
}
