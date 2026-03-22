package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AdminRideDashboardDTO;
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
    List<AdminRideDashboardDTO> getActiveRides(
            String email, String rideStatus,
            int page, int size);
    List<AdminRideDashboardDTO> getCompletedRides(String email, int page, int pageSize);
    List<AdminRideDashboardDTO> getCancelledRides(String email, int page, int pageSize);
}
