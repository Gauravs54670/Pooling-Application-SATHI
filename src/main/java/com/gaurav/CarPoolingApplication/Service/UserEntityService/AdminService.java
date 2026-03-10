package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;

import java.util.List;

public interface AdminService {
    String verifyDriver(String email, DriverVerificationRequest driverVerificationRequest);
    List<DriverVerificationRequest> getListOfUnverifiedDrivers(String email);
}
