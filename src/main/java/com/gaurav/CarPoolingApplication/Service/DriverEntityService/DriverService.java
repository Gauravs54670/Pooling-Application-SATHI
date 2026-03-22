package com.gaurav.CarPoolingApplication.Service.DriverEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.*;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverService {
    DriverProfileDTO getMyDriverProfile(String email);
    DriverProfileDTO updateDriverProfile(String email, DriverProfileUpdateRequest request);
    String updatePhoto(String email, MultipartFile file);
    String changeAvailabilityStatus(String email, String availabilityStatus);
    List<RideResponse> getMyPostedRides(String credential,int page, int pageSize);
//    → calculateDistance() uses Haversine formula on every post
//    → optimization: cache pricePerKm calculation in Redis
//    since it only depends on vehicleCategory and vehicleClass
//    which rarely changes
    RideResponse postRide(String email, RidePostingRequest request);
//    getRideBookings()
//    → fetches all PENDING requests and calculates distance for each in Java
//    → optimization: bounding box filter already added in getAvailableRides()
//    apply same approach here — filter by proximity in DB not in Java
    List<PassengerBookingResponse> getRideBookings(
            String email,
            String rideCode);
    DriverRideRequestDecisionResponse rideSharingDecision(
            String email,
            String rideRequestDecision,
            Long requestId);
    String startRide(String email, String rideCode, Long rideRequestId, String rideOTP);
//    trackRideGPSLocation()
//    → currently uses ConcurrentHashMap in-memory buffer
//    → problem: if server restarts mid-ride, buffer is lost
//    → optimization: move buffer to Redis List
//    Redis survives restarts, shared across instances
    void trackRideGPSLocation(
            String email,
            String rideCode,
            GPSTrackingRequest gpsTrackingRequest);
//    completeRide()
//    → fetches ALL GPS points at once for distance calculation
//    → for a 2-hour ride = 240 points loaded into memory at once
//    → optimization: calculate distance incrementally during tracking
//    keep a running total in Redis, add each segment as it comes in
//    completeRide() just reads the final total — no bulk fetch needed
    RideCompleteResponse completeRide(String email, String rideCode);
    String cancelRide(String email, String rideCode);
    List<DriverRidesHistoryDTO> getDriverRideHistory(String email);
    String reportPassengerNoShow(String email, String rideCode,Long requestId);
//    getActiveRide()
}
