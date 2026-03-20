package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileRequest;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileResponse;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.*;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    UserProfileDTO findMyProfile(String credential);
    UserProfileDTO updateMyProfile(String credential, UpdateProfileRequest request);
    String changePassword(String credential, ChangePasswordRequestDTO request);
    String requestOTP(String email);
    String forgotPassword(String mail, String otp, String newPassword);
    Set<UserRole> getMyRoles(String credential);
    String deactivateMyAccount(String credential);
    String activateMyAccount(String credential);
    DriverProfileResponse registerDriver(
            String credential,
            MultipartFile file,
            DriverProfileRequest driverProfileRequest);
//      - refreshToken()          — JWT refresh token support
//      - uploadProfilePhoto()    — profile picture management
//      - verifyPhoneNumber()     — OTP via SMS for phone verification
}
