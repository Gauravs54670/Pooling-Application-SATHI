package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverProfileEntity;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverVerificationStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserRole;
import com.gaurav.CarPoolingApplication.Exception.UserNotFoundException;
import com.gaurav.CarPoolingApplication.Repository.DriverEntityRepository;
import com.gaurav.CarPoolingApplication.Repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@Slf4j
public class AdminServiceImplementation implements AdminService{

    private final UserEntityRepository userEntityRepository;
    private final DriverEntityRepository driverEntityRepository;
    public AdminServiceImplementation(
            UserEntityRepository userEntityRepository,
            DriverEntityRepository driverEntityRepository) {
        this.driverEntityRepository = driverEntityRepository;
        this.userEntityRepository = userEntityRepository;
    }
//    verify driver
    @Override
    public String verifyDriver(String email, DriverVerificationRequest driverVerificationRequest) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        DriverProfileEntity driverProfile = this.driverEntityRepository.findById(driverVerificationRequest.getDriverId())
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        if(!driverProfile.getDriverVerificationStatus().equals(
                DriverVerificationStatus.PENDING))
            throw new IllegalArgumentException("Driver is already verified or rejected.");
        DriverVerificationStatus verificationStatus;
        try {
            verificationStatus = DriverVerificationStatus.valueOf(driverVerificationRequest.getDriverVerificationStatus()
                            .trim()
                    .toUpperCase());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Invalid verification value. " +
                    "Allowed parameters are PENDING, REJECTED, APPROVED");
        }
        driverProfile.setDriverVerificationStatus(verificationStatus);
        driverProfile.setAccountUpdatedAt(LocalDateTime.now());
        this.driverEntityRepository.save(driverProfile);
        return "Driver verification status updated to " + verificationStatus;
    }
//    get list of all unverified drivers
    @Override
    public List<DriverVerificationRequest> getListOfUnverifiedDrivers(String email) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        return this.driverEntityRepository.getAllUnverifiedDrivers(DriverVerificationStatus.PENDING);
    }

    //    helper methods
//    validate the admin
    private void validateAdmin(UserEntity user) {
        if(!user.getUserRoles().contains(UserRole.ADMIN))
            throw new AccessDeniedException("Access Denied. " +
                    "You are prohibited to access this resource");
    }
}
