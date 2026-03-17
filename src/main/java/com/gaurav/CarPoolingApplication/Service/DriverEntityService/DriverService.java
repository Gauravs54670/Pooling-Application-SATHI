package com.gaurav.CarPoolingApplication.Service.DriverEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.PassengerBookingResponse;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileUpdateRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.GPSTrackingRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideCompleteResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RidePostingRequest;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverService {
    DriverProfileDTO getMyDriverProfile(String email);
    DriverProfileDTO updateDriverProfile(String email, DriverProfileUpdateRequest request);
    String updatePhoto(String email, MultipartFile file);
    String changeAvailabilityStatus(String email, String availabilityStatus);
    List<RideResponse> getMyPostedRides(String credential);
    RideResponse postRide(String email, RidePostingRequest request);
    List<PassengerBookingResponse> getRideBookings(
            String email,
            String rideCode);
    DriverRideRequestDecisionResponse rideSharingDecision(
            String email,
            String rideRequestDecision,
            Long requestId);
    String startRide(String email, String rideCode, Long rideRequestId, String rideOTP);
    RideCompleteResponse completeRide(String email, String rideCode);
    void trackRideGPSLocation(
            String email,
            String rideCode,
            GPSTrackingRequest gpsTrackingRequest);
    String cancelRide(String credential, String rideCode);
}
