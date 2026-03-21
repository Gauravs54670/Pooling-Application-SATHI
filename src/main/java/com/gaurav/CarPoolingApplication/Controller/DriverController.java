package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.PassengerBookingResponse;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileUpdateRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.*;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.Service.DriverEntityService.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/driver")
public class DriverController {
    private final DriverService driverService;
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping("/myProfile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        String credential = authentication.getName();
        DriverProfileDTO driverProfileDTO = this.driverService.getMyDriverProfile(credential);
        return new ResponseEntity<>(Map.of(
                "message", "Profile fetched.",
                "response", driverProfileDTO
        ), HttpStatus.OK);
    }
//    update the driver profile
    @PutMapping("/update-myProfile")
    public ResponseEntity<?> updateDriverProfile(
            @RequestBody DriverProfileUpdateRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        DriverProfileDTO profile = this.driverService.updateDriverProfile(email,request);
        return new ResponseEntity<>(Map.of(
                "message", "Profile Updated.",
                "response", profile
        ),HttpStatus.OK);
    }
//    update driver profile
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateDriverProfilePhoto(
            Authentication authentication,
            @RequestPart("profile photo") MultipartFile multipartFile) {
        String email = authentication.getName();
        String profileUrl = this.driverService.updatePhoto(email, multipartFile);
        return new ResponseEntity<>(Map.of(
                "message", "Profile Updated.",
                "response", profileUrl
        ),HttpStatus.OK);
    }
//    post a ride by driver
    @PostMapping("/post-ride")
    public ResponseEntity<?> postRide(
            Authentication authentication, @RequestBody RidePostingRequest rideRequest) {
        String email = authentication.getName();
        RideResponse rideResponse = this.driverService.postRide(email, rideRequest);
        return new ResponseEntity<>(Map.of(
                "message", "Ride Posted",
                "response", rideResponse
        ),HttpStatus.OK);
    }
//    change driver availability status
    @PutMapping("/change-availability")
    public ResponseEntity<?> changeAvailabilityStatus(
            Authentication authentication,
            @RequestPart("status") String status) {
        String email = authentication.getName();
        String message = this.driverService.changeAvailabilityStatus(email, status);
        return new ResponseEntity<>(Map.of(
                "message", message
        ), HttpStatus.OK);
    }
    @GetMapping("/get-myRides")
    public ResponseEntity<?> getMyRides(Authentication authentication) {
        String email = authentication.getName();
        List<RideResponse> rideResponses = this.driverService.getMyPostedRides(email);
        return new ResponseEntity<>(Map.of(
                "message", "Ride Responses",
                "response", rideResponses
        ),HttpStatus.OK);
    }
//    fetch the requested shared rides
    @GetMapping("/requested-rides")
    public ResponseEntity<?> getRequestedRides(
            Authentication authentication,
            @RequestParam("rideCode") String rideCode) {
        String email = authentication.getName();
        List<PassengerBookingResponse> bookingResponses = this.driverService.getRideBookings(email, rideCode);
        return new ResponseEntity<>(Map.of(
                "message", "Requested Rides",
                "response", bookingResponses
        ),HttpStatus.OK);
    }
    @PutMapping("/request-decision/{requestId}")
    public ResponseEntity<?> rideRequestDecision(
            Authentication authentication,
            @RequestParam("decision") String decision,
            @PathVariable("requestId") Long rideRequestId) {
        String email = authentication.getName();
        DriverRideRequestDecisionResponse requestDecisionResponse =
                this.driverService.rideSharingDecision(
                        email,
                        decision,rideRequestId);
        return new ResponseEntity<>(Map.of(
                "message", "Ride Decision Response",
                "response", requestDecisionResponse
        ),HttpStatus.OK);
    }
//    left to tested
    @PostMapping("/start-ride/{requestId}")
    public ResponseEntity<?> startRide(
            Authentication authentication,
            @RequestParam("rideCode") String rideCode,
            @RequestParam("OTP") String rideOTP,
            @PathVariable("requestId") Long requestId) {
        String email = authentication.getName();
        String message = this.driverService.startRide(email,rideCode, requestId, rideOTP);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
    @PostMapping("/ride-completed")
    public ResponseEntity<?> rideCompleted(
            Authentication authentication,
            @RequestParam("rideCode") String rideCode) {
        String email = authentication.getName();
        RideCompleteResponse rideCompleteResponse = this.driverService.completeRide(email, rideCode);
        return new ResponseEntity<>(Map.of(
                "message", "Ride complete response",
                "response", rideCompleteResponse
        ),HttpStatus.OK);
    }
//    track gps
    @PostMapping("/gps-track/{rideCode}")
    public ResponseEntity<Void> trackGPSLocation(
            Authentication authentication,
            @PathVariable("rideCode") String rideCode,
            @RequestBody GPSTrackingRequest request) {
        this.driverService.trackRideGPSLocation(
                authentication.getName(), rideCode, request);
        return ResponseEntity.ok().build();
    }
//    cancel ride
    @PutMapping("/cancel-ride")
    public ResponseEntity<?> cancelRide(
            Authentication authentication,
            @RequestParam("rideCode") String rideCode) {
        String email = authentication.getName();
        String message = this.driverService.cancelRide(email, rideCode);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    get ride history
    @GetMapping("/ride-history")
    public ResponseEntity<?> getRideHistory(Authentication authentication) {
        String email = authentication.getName();
        List<DriverRidesHistoryDTO> ridesHistoryDTOS = this.driverService
                .getDriverRideHistory(email);
        return new ResponseEntity<>(Map.of(
                "message", "Ride History",
                "response", ridesHistoryDTOS
        ),HttpStatus.OK);
    }
}

