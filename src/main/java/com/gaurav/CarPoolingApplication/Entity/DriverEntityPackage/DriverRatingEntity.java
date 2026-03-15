package com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "driver_rating")
public class DriverRatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverProfileEntity driverProfile;
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private UserEntity passenger;
    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private RideEntity ride;
    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;
    @Size(max = 150)
    private String comment;
    private LocalDateTime reviewedAt;
}
