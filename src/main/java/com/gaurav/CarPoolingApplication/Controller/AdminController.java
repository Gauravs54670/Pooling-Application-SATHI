package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.Service.UserEntityService.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PutMapping("/verify-driver")
    public ResponseEntity<?> verifyUser(
            Authentication authentication,
            @RequestBody DriverVerificationRequest driverVerificationRequest) {
        String email = authentication.getName();
        String message = this.adminService.verifyDriver(email, driverVerificationRequest);
        return new ResponseEntity<>(Map.of(
                "message", message
        ), HttpStatus.OK);
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
}
