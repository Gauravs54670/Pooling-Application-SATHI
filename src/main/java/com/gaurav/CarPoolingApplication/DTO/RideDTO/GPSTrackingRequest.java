package com.gaurav.CarPoolingApplication.DTO.RideDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class GPSTrackingRequest {
    private Double latitude;
    private Double longitude;
}
