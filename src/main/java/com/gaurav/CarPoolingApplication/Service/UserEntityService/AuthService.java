package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.DTO.UserDTO.ChangePasswordRequestDTO;

public interface AuthService {
    String changePassword(String credential, ChangePasswordRequestDTO request);
    String requestOTP(String email);
    String forgotPassword(String mail, String otp, String newPassword);
}
