package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverProfileEntity;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverEntityRepository extends JpaRepository<DriverProfileEntity, Long> {
    boolean existsByDriverLicenseNumber(String driverLicenseNumber);
    boolean existsByVehicleNumber(String vehicleNumber);
    Optional<DriverProfileEntity> findByUserEmail(String email);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileDTO(
                d.driverId,
                d.user.email,
                d.user.phoneNumber,
                d.user.userFullName,
                d.driverProfileUrl,
                d.driverLicenseNumber,
                d.licenseExpirationDate,
                d.vehicleModel,
                d.vehicleNumber,
                d.vehicleSeatCapacity,
                d.vehicleCategory,
                d.vehicleClass,
                d.driverVerificationStatus,
                d.driverAvailabilityStatus,
                d.totalCompletedRides,
                d.totalCancelledRides,
                d.averageRatingOfDriver,
                d.totalReviewCount,
                d.accountCreatedAt
            )
            FROM DriverProfileEntity d
            WHERE d.user.email = :email
            """)
    Optional<DriverProfileDTO> findMyProfile(@Param("email") String mail);
    Optional<DriverProfileEntity> findByVehicleNumber(String vehicleNumber);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest(
                d.driverId,
                CAST(d.driverVerificationStatus AS string)
            )
            FROM DriverProfileEntity d
            WHERE d.driverVerificationStatus = :status
            """)
    List<DriverVerificationRequest> getAllUnverifiedDrivers(
            @Param("status") DriverVerificationStatus status);
//    get driver profile for admin
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO(
                d.user.userFullName,
                d.user.email,
                d.user.phoneNumber,
                d.user.userAccountStatus,
                d.driverProfileUrl,
                d.driverLicenseNumber,
                d.licenseExpirationDate,
                d.isDriverVerified,
                d.driverPhoneNumberVerificationStatus,
                d.vehicleModel,
                d.vehicleNumber,
                d.vehicleCategory,
                d.vehicleClass,
                d.vehicleSeatCapacity,
                d.driverVerificationStatus,
                d.driverAvailabilityStatus,
                d.totalCompletedRides,
                d.totalCancelledRides,
                d.totalDriverRating,
                d.averageRatingOfDriver,
                d.totalReviewCount,
                d.user.accountCreatedAt,
                d.accountUpdatedAt
            )
            FROM DriverProfileEntity d
            WHERE d.driverId = :driverId
            """)
    AdminDriverProfileDTO getDriverProfile(@Param("driverId") Long driverId);
}
