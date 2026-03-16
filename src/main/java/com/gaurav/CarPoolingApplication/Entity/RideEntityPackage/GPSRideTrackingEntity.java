package com.gaurav.CarPoolingApplication.Entity.RideEntityPackage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "gps_tracking_entity")
public class GPSRideTrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackingId;
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", nullable = false)
    private RideEntity ride;
    private LocalDateTime recordedAt;
    private boolean isObstaclePresent = false;
}
