package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.AdminDriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverVerificationRequest;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverProfileEntity;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverVerificationStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserAccountStatus;
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
//    get User's profile
    @Override
    public UserProfileDTO getUserProfile(String email, String credential) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        if (credential == null || credential.isBlank())
            throw new IllegalArgumentException("Invalid credential");
        char firstLetter = credential.charAt(0);
        UserProfileDTO user;
        if(credential.contains("@")) {
            if(!credential.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
                throw new IllegalArgumentException("Invalid email format.");
            return this.userEntityRepository.findUserProfileByEmail(credential)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));
        } else {
            if(!credential.matches("^[0-9]{10}$"))
                throw new IllegalArgumentException("Invalid phone number. Must be 10 digits.");
            return this.userEntityRepository.findUserProfileByPhoneNumber(credential)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));
        }
    }
//    get list of all unverified drivers
    @Override
    public List<DriverVerificationRequest> getListOfUnverifiedDrivers(String email) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        return this.driverEntityRepository.getAllUnverifiedDrivers(DriverVerificationStatus.PENDING);
    }
//    get the driver's profile
    @Override
    public AdminDriverProfileDTO getDriverProfile(String email, Long driverId) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        log.info("Driver Profile fetched");
        return this.driverEntityRepository.getDriverProfile(driverId);
    }
//    verify the user's (driver) phone number
    @Override @Transactional
    public String verifyPhoneNumber(String email, Long driverId) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        DriverProfileEntity driverProfile = this.driverEntityRepository.findById(driverId)
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        if(driverProfile.getDriverPhoneNumberVerificationStatus())
            throw new IllegalStateException("Driver phone number is already verified.");
//        any 3rd party service for phone number verification
        driverProfile.setDriverPhoneNumberVerificationStatus(true);
        this.driverEntityRepository.save(driverProfile);
        return "Driver phone number verified successfully.";
    }

    //    approve the driver for posting rides
    @Override @Transactional
    public String verifyDriver(String email, DriverVerificationRequest driverVerificationRequest) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        DriverProfileEntity driverProfile = this.driverEntityRepository
                .findById(driverVerificationRequest.getDriverId())
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        if(driverProfile.getDriverVerificationStatus() == DriverVerificationStatus.APPROVED)
            throw new IllegalStateException("Driver is already APPROVED.");
        if(driverProfile.getDriverVerificationStatus() == DriverVerificationStatus.REJECTED)
            throw new IllegalStateException("Driver is already REJECTED.");
        if(!driverProfile.getDriverPhoneNumberVerificationStatus())
            throw new IllegalStateException("Driver phone number is not verified yet. " +
                    "Please verify driver phone number first.");
        DriverVerificationStatus verificationStatus;
        try {
            verificationStatus = DriverVerificationStatus.valueOf(
                    driverVerificationRequest.getDriverVerificationStatus().trim().toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid driver verification status. " +
                    "Allowed values are APPROVED, REJECTED");
        }
        switch (verificationStatus) {
            case APPROVED -> {
                driverProfile.setIsDriverVerified(true);
                driverProfile.setDriverVerificationStatus(
                        DriverVerificationStatus.APPROVED);
            }
            case REJECTED -> {
                driverProfile.setIsDriverVerified(false);
                driverProfile.setDriverVerificationStatus(
                        DriverVerificationStatus.REJECTED);
            }
        }
        this.driverEntityRepository.save(driverProfile);
        return "Driver Profile is " + verificationStatus.name();
    }
//    change user' account status
    @Override @Transactional
    public String suspendUser(String email, Long userId) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        UserEntity user = this.userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(user.getEmail().equals(email))
            throw new AccessDeniedException("Admin cannot suspend their own account.");
        if(user.getUserRoles().contains(UserRole.ADMIN))
            throw new AccessDeniedException("Admin accounts cannot be suspended.");
        if(user.getUserAccountStatus() == UserAccountStatus.SUSPENDED)
            throw new IllegalStateException("User is already SUSPENDED.");
        user.setUserAccountStatus(UserAccountStatus.SUSPENDED);
        user.setIsAdminSuspendedAccount(true);
        this.userEntityRepository.save(user);
        return "User suspended successfully";
    }

    @Override
    public String activateUser(String email, Long userId) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        UserEntity user = this.userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Profile not found."));
        if(user.getUserAccountStatus() == UserAccountStatus.ACTIVATED)
            throw new IllegalStateException("User is already ACTIVATED.");
        user.setUserAccountStatus(UserAccountStatus.ACTIVATED);
        user.setIsAdminSuspendedAccount(false);
        return "Account Activated.";
    }

    //    helper methods
//    validate the admin
    private void validateAdmin(UserEntity user) {
        if(!user.getUserRoles().contains(UserRole.ADMIN))
            throw new AccessDeniedException("Access Denied. " +
                    "You are prohibited to access this resource");
    }
}
