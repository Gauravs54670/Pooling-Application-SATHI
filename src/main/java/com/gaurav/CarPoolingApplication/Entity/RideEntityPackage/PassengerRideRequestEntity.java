package com.gaurav.CarPoolingApplication.Entity.RideEntityPackage;

import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
@Entity @Table(name = "passenger_ride_request_entity")
public class PassengerRideRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    private String rideCode;
    @ManyToOne
    private RideEntity ride;
    @ManyToOne
    private UserEntity passenger;
    private Integer requestedSeats;
    private Double sourceLat;
    private Double sourceLong;
    private String sourceAddress;
    private Double destinationLat;
    private Double destinationLong;
    private String destinationAddress;
    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;
    private LocalDateTime rideRequestedAt;
    private LocalDateTime decisionTime;
}
