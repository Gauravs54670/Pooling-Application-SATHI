package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.BookingResponse;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.PassengerRideRequestEntity;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRideRequestRepository extends JpaRepository<PassengerRideRequestEntity, Long> {
    Optional<PassengerRideRequestEntity> findByPassengerAndRideCode(UserEntity passenger, String rideCode);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.DriverDTO.BookingResponse(
                r.requestId,
                r.rideCode,
                r.passenger.userFullName,
                r.requestedSeats,
                r.sourceLong,
                r.sourceLat,
                r.sourceAddress,
                r.destinationLat,
                r.destinationLong,
                r.destinationAddress,
                r.rideRequestStatus,
                r.rideRequestedAt
            )
            FROM PassengerRideRequestEntity r
            WHERE r.rideCode = :rideCode
            AND r.ride.driverProfileEntity.driverId = :driverId
            """)
    List<BookingResponse> getAllBookingRequests(
            @Param("rideCode") String rideCode,
            @Param("driverId") Long driverId
    );
    @Query("""
                SELECT new com.gaurav.CarPoolingApplication.DTO.PassengerDTO
                .PassengerRideRequestDecisionResponse(
                    r.requestId,
                    r.rideCode,
                    CASE
                        WHEN r.rideRequestStatus = 'ACCEPTED'
                        THEN r.ride.driverProfileEntity.user.userFullName
                        ELSE null
                    END,
                    CASE
                        WHEN r.rideRequestStatus = 'ACCEPTED'
                        THEN r.ride.driverProfileEntity.user.phoneNumber
                        ELSE null
                    END,
                    r.rideRequestStatus,
                    r.decisionTime
                )
                FROM PassengerRideRequestEntity r
                WHERE r.requestId = :requestId
                AND r.rideCode = :rideCode
            """)
    PassengerRideRequestDecisionResponse getDecisionResponse(
            @Param("requestId") Long requestId,
            @Param("rideCode") String rideCode);
}
