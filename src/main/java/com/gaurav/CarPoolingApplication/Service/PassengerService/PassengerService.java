package com.gaurav.CarPoolingApplication.Service.PassengerService;

import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequest;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;

import java.util.List;

public interface PassengerService {
    List<AvailableRidesDTO> getAvailableRides(String email);
    PassengerRideRequestResponse requestRide(
            String email,
            PassengerRideRequest passengerRideRequest);
    PassengerRideRequestDecisionResponse getRideSharingDecision(
            String email,
            Long requestId,
            String rideCode);
}
