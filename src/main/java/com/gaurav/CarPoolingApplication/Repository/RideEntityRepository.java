package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideResponse;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverProfileEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideEntityRepository extends JpaRepository<RideEntity, Long> {

//    check if the driver already posted a ride or not
    boolean existsByDriverProfileEntityAndRideStatus(DriverProfileEntity driverProfile, RideStatus rideStatus);
    Optional<RideEntity> findByDriverProfileEntity_DriverIdAndRideCode(Long driverId, String rideCode);
    @Query("""
                SELECT new com.gaurav.CarPoolingApplication.DTO.RideDTO.RideResponse(
                    r.rideCode,
                    r.sourceLat,
                    r.sourceLong,
                    r.sourceAddress,
                    r.destinationLat,
                    r.destinationLong,
                    r.destinationAddress,
                    r.departureTime,
                    r.totalSeats,
                    r.availableSeats,
                    r.estimatedTotalDistance,
                    r.pricePerKm,
                    r.estimatedTotalFare,
                    r.actualTotalDistance,
                    r.actualTotalFare,
                    r.rideCreatedAt
                )
                FROM RideEntity r
                WHERE r.driverProfileEntity.driverId = :driverId
                AND r.isRideDeleted = false
                ORDER BY r.departureTime DESC
            """)
    List<RideResponse> getDriverPostedRides(@Param("driverId") Long driverId);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO(
                r.rideCode,
                r.driverProfileEntity.user.userFullName,
                r.driverProfileEntity.averageRatingOfDriver,
                r.sourceAddress,
                r.destinationAddress,
                r.departureTime,
                r.totalSeats,
                r.availableSeats,
                r.pricePerKm,
                r.driverProfileEntity.vehicleModel,
                r.vehicleCategory,
                r.vehicleClass,
                r.rideStatus
            )
            FROM RideEntity r
            WHERE r.isRideDeleted = false
            AND r.rideStatus = com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus.ACTIVE
            AND r.availableSeats > 0
            """)
    List<AvailableRidesDTO> getAllAvailableRides();
    Optional<RideEntity> findByRideCode(String rideCode);
    @Query("""
        SELECT r FROM RideEntity r
        WHERE r.rideStatus = 'ACTIVE'
        AND r.availableSeats > 0
        AND r.departureTime > CURRENT_TIMESTAMP
        AND r.sourceLat BETWEEN :sourceLat - :radius AND :sourceLat + :radius
        AND r.sourceLong BETWEEN :sourceLong - :radius AND :sourceLong + :radius
        AND r.destinationLat BETWEEN :destinationLat - :radius AND :destinationLat + :radius
        AND r.destinationLong BETWEEN :destinationLong - :radius AND :destinationLong + :radius
        """)
    List<RideEntity> findAvailableRides(
            @Param("sourceLat") double sourceLat,
            @Param("sourceLong") double sourceLong,
            @Param("destinationLat") double destinationLat,
            @Param("destinationLong") double destinationLong,
            @Param("radius") double radius);
}
