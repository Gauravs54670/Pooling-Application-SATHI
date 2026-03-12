package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class DriverRatingClass {
    private Integer rating;
    private String comment;
    private String rideCode;
}
