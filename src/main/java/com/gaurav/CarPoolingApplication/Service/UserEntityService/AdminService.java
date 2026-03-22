package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO;

import java.util.List;

public interface AdminService {
    UserProfileDTO getUserProfile(String email, String credential);
    List<DriverVerificationRequest> getListOfUnverifiedDrivers(String email);
    AdminDriverProfileDTO getDriverProfile(String email, Long driverId);
    String verifyPhoneNumber(String email, Long driverId);
    String verifyDriver(
            String email,
            DriverVerificationRequest driverVerificationRequest);
    String suspendUser(String email, Long userId);
    String activateUser(String email, Long userId);
//- suspendUser()           — ban/suspend accounts
//- getAllRides()            — monitor all rides
//- getDashboardStats()     — platform analytics
//- getAllDisputes()         — handle passenger/driver disputes
}
