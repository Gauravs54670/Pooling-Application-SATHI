package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class BookingResponse {
    private Long requestId;
    private String rideCode;
    private String passengerName;
    private Integer requestedSeats;
    private Double passengerSourceLong;
    private Double passengerSourceLat;
    private String passengerSourceAddress;
    private Double passengerDestinationLat;
    private Double passengerDestinationLong;
    private String passengerDestinationAddress;
    private Double distanceFromDriver;
    private String rideRequestStatus;
    private LocalDateTime requestCreatedAt;
    public BookingResponse(
            Long requestId,
            String rideCode,
            String passengerName,
            Integer requestedSeats,
            Double passengerSourceLong,
            Double passengerSourceLat,
            String passengerSourceAddress,
            Double passengerDestinationLat,
            Double passengerDestinationLong,
            String passengerDestinationAddress,
            RideRequestStatus rideRequestStatus,
            LocalDateTime requestCreatedAt) {
        this.requestId = requestId;
        this.rideCode = rideCode;
        this.passengerName = passengerName;
        this.requestedSeats = requestedSeats;
        this.passengerSourceLong = passengerSourceLong;
        this.passengerSourceLat = passengerSourceLat;
        this.passengerSourceAddress = passengerSourceAddress;
        this.passengerDestinationLat = passengerDestinationLat;
        this.passengerDestinationLong = passengerDestinationLong;
        this.passengerDestinationAddress = passengerDestinationAddress;
        this.rideRequestStatus = rideRequestStatus.name();
        this.requestCreatedAt = requestCreatedAt;
    }
}
