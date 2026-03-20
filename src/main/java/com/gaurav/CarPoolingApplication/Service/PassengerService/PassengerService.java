package com.gaurav.CarPoolingApplication.Service.PassengerService;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRatingClass;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.*;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideSearchRequestDTO;
import java.util.List;

public interface PassengerService {
    List<AvailableRidesDTO> getAvailableRides(
            String email,
            RideSearchRequestDTO rideSearchRequestDTO);
    PassengerRideRequestResponse requestRide(
            String email,
            PassengerRideRequest passengerRideRequest);
    PassengerRideRequestDecisionResponse getRideSharingDecision(
            String email,
            Long requestId,
            String rideCode);
    MyRideRequests getMyRideRequestStatus(String email, Long requestId);
    PassengerExitRideResponseDTO existRide(
            String email,
            PassengerRideExistRequestDTO passengerRideExistRequestDTO);
    DriverRatingClass rateDriver(
            String email, Integer rating,
            String comment, String rideCode);
    List<PassengerRideHistoryDTO> getRideHistory(String email);
//    - cancelRideRequest()     — passenger cancels their own PENDING request
//    - getActiveRide()         — get currently active ride for passenger
//    - rateDriver()            — already present
}
