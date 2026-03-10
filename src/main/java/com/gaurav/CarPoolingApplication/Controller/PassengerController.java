package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequest;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;
import com.gaurav.CarPoolingApplication.Service.PassengerService.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/passenger")
public class PassengerController {
    private final PassengerService passengerService;
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
//    get all available rides
    @GetMapping("/get-availableRides")
    public ResponseEntity<?> getAvailableRides(Authentication authentication) {
        String email = authentication.getName();
        List<AvailableRidesDTO> ridesDTOS = this.passengerService.getAvailableRides(email);
        return new ResponseEntity<>(Map.of(
                "message", "Available Rides",
                "response", ridesDTOS
        ), HttpStatus.OK);
    }
//    post the ride request
    @PostMapping("/request-ride")
    public ResponseEntity<?> postRide(
            Authentication authentication,
            @RequestBody PassengerRideRequest passengerRideRequest) {
        String email = authentication.getName();
        PassengerRideRequestResponse requestResponse =
                this.passengerService.requestRide(email, passengerRideRequest);
        return new ResponseEntity<>(Map.of(
                "message", "Request Successfully done",
                "response", requestResponse
        ),HttpStatus.OK);
    }
//    get ride-sharing decision request response
    @GetMapping("/get-decision-response/{requestId}")
    public ResponseEntity<?> getDecisionResponse(
            Authentication authentication,
            @PathVariable("requestId") Long requestId,
            @RequestParam String rideCode) {
        String email = authentication.getName();
        PassengerRideRequestDecisionResponse response =
                this.passengerService.getRideSharingDecision(email,requestId,rideCode);
        return new ResponseEntity<>(Map.of(
                "message", "Ride request response",
                "response", response
        ),HttpStatus.OK);
    }
}
