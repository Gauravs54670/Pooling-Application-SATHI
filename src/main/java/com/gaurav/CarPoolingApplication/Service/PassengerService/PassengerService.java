package com.gaurav.CarPoolingApplication.Service.PassengerService;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRatingClass;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.*;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideSearchRequestDTO;
import java.util.List;

public interface PassengerService {
//    getAvailableRides()
//    → bounding box filter is good but still loads full RideEntity objects
//    → optimization: use JPQL projection to fetch only needed fields
//    instead of full entity — reduces data transfer from DB
    List<AvailableRidesDTO> getAvailableRides(
            String email,
            RideSearchRequestDTO rideSearchRequestDTO);
//    requestRide()
//    → checks duplicate request with findByPassengerAndRideCode()
//    → this query runs on every request — add DB index on
//    (passenger_id, ride_id) composite key in PassengerRideRequestEntity
    PassengerRideRequestResponse requestRide(
            String email,
            PassengerRideRequest passengerRideRequest);
    PassengerRideRequestDecisionResponse getRideSharingDecision(
            String email,
            Long requestId,
            String rideCode);
    MyRideRequests getMyRideRequestStatus(String email, Long requestId);
//    exitRide()
//    → calculateDistance() called every time passenger exits
//    → no caching — same coordinates could be recalculated multiple times
//    → acceptable for now but worth noting
    PassengerExitRideResponseDTO existRide(
            String email,
            PassengerRideExistRequestDTO passengerRideExistRequestDTO);
    DriverRatingClass rateDriver(
            String email, Integer rating,
            String comment, String rideCode);
    List<PassengerRideHistoryDTO> getRideHistory(String email,int page, int pageSize);
    String cancelRideRequest(String email, Long requestId);
//    getActiveRunningRide()
}
