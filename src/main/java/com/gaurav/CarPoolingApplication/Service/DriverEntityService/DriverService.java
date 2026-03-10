package com.gaurav.CarPoolingApplication.Service.DriverEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.BookingResponse;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileUpdateRequest;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerBookingRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideRequest;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverService {
    DriverProfileDTO getMyDriverProfile(String email);
    DriverProfileDTO updateDriverProfile(String email, DriverProfileUpdateRequest request);
    String updatePhoto(String email, MultipartFile file);
    RideResponse postRide(String email, RideRequest request);
    String changeAvailabilityStatus(String email, String availabilityStatus);
    List<RideResponse> getMyPostedRides(String credential);
    String cancelRide(String credential, String rideCode);
    List<BookingResponse> getRideBookings(
            String email,
            String rideCode);
    DriverRideRequestDecisionResponse rideSharingDecision(
            String email,
            String rideRequestDecision,
            PassengerBookingRequest passengerBookingRequest);
    /*
    // 9️⃣ Accept / Reject Booking
    String updateBookingStatus(
            String credential,
            Long bookingId,
            BookingStatus status);

    // 🔟 View Earnings Summary
    DriverEarningsResponse getEarningsSummary(String credential);
    */
}
