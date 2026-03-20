package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRatingClass;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.*;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideSearchRequestDTO;
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
    public ResponseEntity<?> getAvailableRides(
            Authentication authentication,
            @RequestBody RideSearchRequestDTO rideSearchRequestDTO) {
        String email = authentication.getName();
        List<AvailableRidesDTO> ridesDTOS = this.passengerService.getAvailableRides(email, rideSearchRequestDTO);
        return new ResponseEntity<>(Map.of(
                "message", "Available Rides",
                "response", ridesDTOS
        ), HttpStatus.OK);
    }
//    post the ride request
    @PostMapping("/request-ride")
    public ResponseEntity<?> requestRide(
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
//    get my requested ride status
    @GetMapping("/my-rideRequests/{requestId}")
    public ResponseEntity<?> myRequestedRides(
            Authentication authentication,
            @PathVariable("requestId") Long requestId) {
        String email = authentication.getName();
        MyRideRequests myRideRequests =
                this.passengerService.getMyRideRequestStatus(email, requestId);
        return new ResponseEntity<>(Map.of(
                "message", "Requests Fetched",
                "response", myRideRequests
        ),HttpStatus.OK);
    }
//    rate driver
    @PostMapping("/rate")
    public ResponseEntity<?> rateDriver(
            Authentication authentication,
            @RequestParam("rideCode") String rideCode,
            @RequestParam("rating") Integer rating,
            @RequestParam("comment") String comment) {
        String email = authentication.getName();
        DriverRatingClass driverRatingClass = this.passengerService
                .rateDriver(email, rating, comment, rideCode);
        return new ResponseEntity<>(Map.of(
                "message", "Review done successfully",
                "response", driverRatingClass
        ),HttpStatus.OK);
    }
//    get ride history
    @GetMapping("/ride-history")
    public ResponseEntity<?> getRideHistory(
            Authentication authentication,
            @RequestParam("rideStatus") String rideStatus){
        String email = authentication.getName();
        List<PassengerRideHistoryDTO> passengerRideHistories = this
                .passengerService.getRideHistory(email);
        return new ResponseEntity<>(Map.of(
                "message", "Ride History",
                "response", passengerRideHistories
        ),HttpStatus.OK);
    }
//    to exit from a ride
    @PutMapping("/exit-ride")
    public ResponseEntity<?> exitRide(
            Authentication authentication,
            @RequestBody PassengerRideExistRequestDTO passengerRideExistRequestDTO) {
        String email = authentication.getName();
        PassengerExitRideResponseDTO responseDTO = this.passengerService
                .existRide(email, passengerRideExistRequestDTO);
        return new ResponseEntity<>(Map.of(
                "message", "Ride completed successfully",
                "response", responseDTO
        ),HttpStatus.OK);
    }
}
