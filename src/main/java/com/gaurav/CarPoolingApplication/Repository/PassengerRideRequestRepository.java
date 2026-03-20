package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.PassengerBookingResponse;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.MyRideRequests;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideHistoryDTO;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.PassengerRideRequestEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRideRequestRepository extends JpaRepository<PassengerRideRequestEntity, Long> {
    Optional<PassengerRideRequestEntity> findByPassengerAndRideCode(UserEntity passenger, String rideCode);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.DriverDTO.PassengerBookingResponse(
                r.requestId,
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
            AND r.rideRequestStatus = com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus.PENDING
            """)
    List<PassengerBookingResponse> getAllBookingRequests(
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
//    get ride request response (driver accepted the ride request or not)
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.PassengerDTO.MyRideRequests(
                r.requestId,
                r.ride.driverProfileEntity.user.userFullName,
                r.ride.driverProfileEntity.user.phoneNumber,
                r.ride.driverProfileEntity.driverProfileUrl,
                r.rideRequestStatus
            )
            FROM PassengerRideRequestEntity r
            WHERE r.requestId = :requestId
            AND r.rideRequestStatus = :rideRequestStatus
            """)
    MyRideRequests ridesRequestedByDriver(
            @Param("requestId") Long requestId,
            @Param("rideRequestStatus") RideRequestStatus rideRequestStatus);
    //    get passenger ride history
        @Query("""
                SELECT new com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideHistoryDTO(
                    r.requestId,
                    r.rideRequestStatus,
                    r.sourceAddress,
                    r.destinationAddress,
                    r.passengerActualFare,
                    r.passengerActualDistance,
                    r.requestedSeats,
                    r.ride.vehicleClass,
                    r.ride.vehicleCategory,
                    r.ride.driverProfileEntity.vehicleModel,
                    r.rideRequestedAt,
                    r.ride.departureTime,
                    r.ride.rideCompletedAt,
                    r.ride.driverProfileEntity.user.userFullName,
                    r.ride.driverProfileEntity.driverProfileUrl,
                    r.ride.driverProfileEntity.averageRatingOfDriver,
                    r.rideBoardingAt,
                    r.rideExitedAt
                )
                FROM PassengerRideRequestEntity r
                WHERE r.passenger.userId = :userId
                AND (r.rideRequestStatus = com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus.REJECTED
                OR r.rideRequestStatus = com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus.CANCELLED
                OR r.rideRequestStatus = com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus.COMPLETED)
                """)
        List<PassengerRideHistoryDTO> getPassengerRideHistory(@Param("userId") Long userId);
    Optional<PassengerRideRequestEntity> findByPassengerAndRequestId(UserEntity passenger, Long requestId);
//    check if there is a passenger exist into a driver's vehicle before cancelling the ride
    boolean existsByRideAndRideRequestStatus(RideEntity ride, RideRequestStatus rideRequestStatus);
//    cancel all the pending requests of ride-sharing
    @Modifying
    @Query("""
            UPDATE PassengerRideRequestEntity p
            SET p.rideRequestStatus = 'CANCELLED'
            WHERE p.ride.rideId = :rideId AND p.rideRequestStatus = 'PENDING'
            """)
    void cancelAllPendingRideSharingRequests(@Param("rideId") Long rideId);
}
