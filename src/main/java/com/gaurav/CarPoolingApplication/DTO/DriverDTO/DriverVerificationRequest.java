package com.gaurav.CarPoolingApplication.DTO.DriverDTO;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverVerificationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DriverVerificationRequest {
    private Long driverId;
    private String driverVerificationStatus;
    public DriverVerificationRequest(
            Long driverId,
            String driverVerificationStatus) {
        this.driverId = driverId;
        this.driverVerificationStatus = driverVerificationStatus;
    }
}
