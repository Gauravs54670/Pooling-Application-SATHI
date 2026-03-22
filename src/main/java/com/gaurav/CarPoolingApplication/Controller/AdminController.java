package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AdminRideDashboardDTO;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO;
import com.gaurav.CarPoolingApplication.Service.UserEntityService.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController @RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
//    get user's profile
    @GetMapping("/get-profile")
    public ResponseEntity<?> getUserProfile(
            Authentication authentication, @RequestParam("credential") String credential) {
        String email = authentication.getName();
        UserProfileDTO user = this.adminService.getUserProfile(email, credential);
        return new ResponseEntity<>(Map.of(
                "message", "Profile fetched",
                "response", user
        ),HttpStatus.OK);
    }
//    get all the unverified drivers
    @GetMapping("/all-unverifiedDrivers")
    public ResponseEntity<?> getAllUnverifiedDrivers(Authentication authentication) {
        String email = authentication.getName();
        List<DriverVerificationRequest> verificationRequests = this.adminService.getListOfUnverifiedDrivers(email);
        return new ResponseEntity<>(Map.of(
                "message", "Unverified Drivers",
                "response", verificationRequests
        ),HttpStatus.OK);
    }
//    get driver's profile
        @GetMapping("/driver-profile/{driverId}")
        public ResponseEntity<?> getDriverProfile(
                Authentication authentication,
                @PathVariable("driverId") Long driverId) {
        String email = authentication.getName();
        AdminDriverProfileDTO adminDriverProfileDTO = this.adminService
                .getDriverProfile(email, driverId);
        return new ResponseEntity<>(Map.of(
                "message","Profile Fetched",
                "response", adminDriverProfileDTO
        ),HttpStatus.OK);
    }
//    verify the driver's phone number
    @PostMapping("/verify-phoneNumber")
    public ResponseEntity<?> verifyPhoneNumber(
            Authentication authentication,
            @RequestParam("driverId") Long driverId) {
        String email = authentication.getName();
        String message = this.adminService.verifyPhoneNumber(email, driverId);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    approve driver for posting rides
    @PostMapping("/verify-driver")
    public ResponseEntity<?> verifyDriver(
            Authentication authentication,
            @RequestBody DriverVerificationRequest driverVerificationRequest) {
        String email = authentication.getName();
        String message = this.adminService.verifyDriver(
                email, driverVerificationRequest);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    suspend user
    @PostMapping("/suspend-user")
    public ResponseEntity<?> suspendUser(
            Authentication authentication,
            @RequestParam("userId") Long userId) {
        String email = authentication.getName();
        String response = this.adminService.suspendUser(email, userId);
        return new ResponseEntity<>(Map.of(
                "message", response
        ),HttpStatus.OK);
    }
//    activate user
    @PostMapping("/activate-user")
    public ResponseEntity<?> activateUser(
            Authentication authentication,
            @RequestParam("userId") Long userId) {
        String email = authentication.getName();
        String message = this.adminService.activateUser(email, userId);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    get active rides
    @GetMapping("/get-activeRides")
    public ResponseEntity<?> getActiveRides(
            Authentication authentication,
            @RequestParam("rideStatus") String rideStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String email = authentication.getName();
        List<AdminRideDashboardDTO> rides = this.adminService.getActiveRides(email, rideStatus, page, size);
        return new ResponseEntity<>(Map.of(
                "message", "Rides fetched",
                "response", rides
        ), HttpStatus.OK);
    }
//    get completed rides
    @GetMapping("/completed-rides")
    public ResponseEntity<?> getCompletedRides(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        String email = authentication.getName();
        List<AdminRideDashboardDTO> rides = this.adminService.getCompletedRides(email, page, pageSize);
        return new ResponseEntity<>(Map.of(
                "message", "Rides fetched",
                "response", rides
        ), HttpStatus.OK);
    }
//    get cancelled rides
    @GetMapping("cancelled-rides")
    public ResponseEntity<?> getCancelledRides(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        String email = authentication.getName();
        List<AdminRideDashboardDTO> rides = this.adminService.getCancelledRides(email, page, pageSize);
        return new ResponseEntity<>(Map.of(
                "message", "Rides fetched",
                "response", rides
        ), HttpStatus.OK);
    }
}
