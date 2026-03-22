package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.cloudinary.Cloudinary;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileRequest;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileResponse;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.*;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.*;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserAccountStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserRole;
import com.gaurav.CarPoolingApplication.Exception.UserNotFoundException;
import com.gaurav.CarPoolingApplication.Repository.DriverEntityRepository;
import com.gaurav.CarPoolingApplication.Repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Transactional
@Service
@Slf4j
public class UserServiceImplementation implements
        UserService,AuthService{

    private final Cloudinary cloudinary;
    private final DriverEntityRepository driverEntityRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;
    public UserServiceImplementation(
            Cloudinary cloudinary,
            DriverEntityRepository driverEntityRepository,
            JavaMailSender javaMailSender,
            PasswordEncoder passwordEncoder,
            UserEntityRepository userEntityRepository) {
        this.cloudinary = cloudinary;
        this.driverEntityRepository = driverEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEntityRepository = userEntityRepository;
        this.javaMailSender = javaMailSender;
    }
//    register user
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        boolean checkAccount = this.userEntityRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent();
        if(checkAccount) throw new AccessDeniedException("Account already exists with entered email. " +
                "Please check the credentials");
        checkAccount = this.userEntityRepository.findByPhoneNumber(userRegistrationRequest.getPhoneNumber()).isPresent();
        if(checkAccount) throw new AccessDeniedException("Account already exists with entered contact number.");
        if(userRegistrationRequest.getPhoneNumber().length() > 10)
            throw new IllegalArgumentException("Invalid phone number. Please check your contact number.");
        UserEntity user = UserEntity.builder()
                .email(userRegistrationRequest.getEmail())
                .userFullName(userRegistrationRequest.getUserFullName())
                .phoneNumber(userRegistrationRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .userAccountStatus(UserAccountStatus.ACTIVATED)
                .userRoles(Set.of(UserRole.USER_ROLE,UserRole.PASSENGER_ROLE))
                .accountCreatedAt(LocalDateTime.now())
                .accountUpdatedAt(LocalDateTime.now())
                .build();
        user = this.userEntityRepository.save(user);
        return UserRegistrationResponse.builder()
                .email(user.getEmail())
                .userId(user.getUserId())
                .phoneNumber(user.getPhoneNumber())
                .message("Account Registration done successfully.")
                .build();
    }
//    fetch user profile
    @Override
    public UserProfileDTO findMyProfile(String credential) {
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
//    update my profile (by user)
    @Override
    public UserProfileDTO updateMyProfile(String credential, UpdateProfileRequest request) {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(credential,credential)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());
        if(request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()){
            if(request.getPhoneNumber().length() > 10) throw new IllegalArgumentException("Invalid phone number." +
                    " Please check the phone number.");
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getUserFullName() != null && !request.getUserFullName().isEmpty())
            user.setUserFullName(request.getUserFullName());
        user.setAccountUpdatedAt(LocalDateTime.now());
        user = this.userEntityRepository.save(user);
        return UserProfileDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userFullName(user.getUserFullName())
                .userAccountStatus(user.getUserAccountStatus())
                .accountCreatedAt(user.getAccountCreatedAt())
                .accountUpdatedAt(user.getAccountUpdatedAt())
                .build();
    }
//    change password of user's account
    @Override
    public String changePassword(String credential, ChangePasswordRequestDTO request) {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(credential,credential)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        boolean isPasswordMatched = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if(!isPasswordMatched) throw new AccessDeniedException("Old Password doesn't match. " +
                "Please try again or click forgot password.");
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return "Password Changed successfully";
    }
//    send otp to mail
    @Override
    public String requestOTP(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        LocalDateTime now = LocalDateTime.now();
        if (user.getOtpExpirationTime() != null) {
            LocalDateTime otpGeneratedTime = user.getOtpExpirationTime().minusMinutes(5);
            LocalDateTime nextAllowedRequestTime = otpGeneratedTime.plusMinutes(1);
            if (now.isBefore(nextAllowedRequestTime))
                throw new IllegalStateException("You can request OTP only after 1 minute.");
        }
        String otp = generateOtp();
        user.setOtpColumn(otp);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5));
        this.userEntityRepository.save(user);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Password change verification");
        mail.setText(
                "Your 6-digit verification code is: " + otp +
                        "\nThis code is valid for 5 minutes.");
        javaMailSender.send(mail);
        return "Mail sent to " + email +" .Please check your mail.";
    }

    @Override
    public String forgotPassword(String mail, String otp, String newPassword) {
        UserEntity user = this.userEntityRepository.findByEmail(mail)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        if (user.getOtpColumn() == null || user.getOtpExpirationTime() == null)
            throw new AccessDeniedException("OTP not requested.");
        if(user.getOtpExpirationTime().isBefore(LocalDateTime.now()))
            throw new AccessDeniedException("OTP Expired. Please try again.");
        if(!user.getOtpColumn().equals(otp))
            throw new AccessDeniedException("INVALID OTP. Please check the entered otp.");
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtpColumn(null);
        user.setOtpExpirationTime(null);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return "Password changed successfully";
    }

    @Override
    public Set<UserRole> getMyRoles(String credential) {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(credential, credential)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        validateUserAccount(user);
        return user.getUserRoles();
    }

    @Override
    public String deactivateMyAccount(String credential) {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(credential, credential)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(user.getUserAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("Account SUSPENDED.");
        if(user.getUserAccountStatus().equals(UserAccountStatus.DEACTIVATED))
            throw new RuntimeException("Account already deactivated.");
        user.setUserAccountStatus(UserAccountStatus.DEACTIVATED);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return "Account Deactivated Successfully";
    }

    @Override
    public String activateMyAccount(String credential) {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(credential, credential)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(user.getUserAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("Account SUSPENDED.");
        if(user.getUserAccountStatus().equals(UserAccountStatus.ACTIVATED))
            throw new RuntimeException("Account already active.");
        user.setUserAccountStatus(UserAccountStatus.ACTIVATED);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return "Account Activated Successfully";
    }
    //    register as driver
    @Override @Transactional
    public DriverProfileResponse registerDriver(String credential, MultipartFile file, DriverProfileRequest driverProfileRequest) {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(credential, credential)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        boolean isPresent = this.driverEntityRepository.findByUserEmail(user.getEmail()).isPresent();
        if(isPresent) throw new AccessDeniedException("Profile already registered. Please check your credentials.");
        VehicleCategory vehicleCategory;
        VehicleClass vehicleClass;
        try {
            vehicleCategory = VehicleCategory
                    .valueOf(driverProfileRequest.getVehicleCategory().trim().toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            String allowedValues = Arrays.stream(VehicleCategory.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    "Invalid Vehicle Category. Allowed values are: " + allowedValues
            );
        }
        try {
            vehicleClass = VehicleClass
                    .valueOf(driverProfileRequest.getVehicleClass().trim().toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            String allowedValues = Arrays.stream(VehicleClass.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    "Invalid Vehicle Class. Allowed values are: " + allowedValues
            );
        }
        String profileUrl, cloudId;
        try{
            log.info("Profile Photo upload");
            Map<String, String> uploadOption = new HashMap<>();
            uploadOption.put("resource_type", "image");
            Map result = cloudinary.uploader().upload(file.getBytes(), uploadOption);
            profileUrl = (String) result.getOrDefault("secure_url", null);
            cloudId = (String) result.getOrDefault("public_id", null);
            if (profileUrl == null || cloudId == null)
                throw new RuntimeException("Media upload failed: Cloudinary response incomplete.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DriverProfileEntity driverProfileEntity = DriverProfileEntity.builder()
                .user(user)
                .driverProfileUrl(profileUrl)
                .driverProfileCloudId(cloudId)
                .driverLicenseNumber(driverProfileRequest.getDriverLicenseNumber())
                .licenseExpirationDate(driverProfileRequest.getLicenseExpirationDate())
                .vehicleModel(driverProfileRequest.getVehicleModel())
                .vehicleNumber(driverProfileRequest.getVehicleNumber())
                .vehicleCategory(vehicleCategory)
                .vehicleClass(vehicleClass)
                .vehicleSeatCapacity(driverProfileRequest.getVehicleSeatCapacity())
                .driverVerificationStatus(DriverVerificationStatus.PENDING)
                .driverAvailabilityStatus(DriverAvailabilityStatus.OFFLINE)
                .accountCreatedAt(LocalDateTime.now())
                .accountUpdatedAt(LocalDateTime.now())
                .build();
        driverProfileEntity = this.driverEntityRepository.save(driverProfileEntity);
        user.getUserRoles().add(UserRole.DRIVER_ROLE);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return DriverProfileResponse.builder()
                .driverProfileUrl(profileUrl)
                .driverLicenseNumber(driverProfileEntity.getDriverLicenseNumber())
                .licenseExpirationDate(driverProfileEntity.getLicenseExpirationDate())
                .vehicleModel(driverProfileEntity.getVehicleModel())
                .vehicleNumber(driverProfileEntity.getVehicleNumber())
                .vehicleCategory(driverProfileEntity.getVehicleCategory())
                .vehicleClass(driverProfileEntity.getVehicleClass())
                .build();
    }
    //    helper methods
//    generating otp
    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
//    validate user's account status
    private void validateUserAccount(UserEntity user) {
        if(user.getUserAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("User's account is suspended. Please try after some time.");
        if(user.getUserAccountStatus().equals(UserAccountStatus.DEACTIVATED))
            throw new AccessDeniedException("User's account is deactivated. Please try after some time.");
    }
}
