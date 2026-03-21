package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class DriverRatingClass {
    private Integer rating;
    private String comment;
    private String rideCode;
}
