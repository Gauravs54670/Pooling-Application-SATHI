package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserRegistrationRequest;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserRegistrationResponse;
import com.gaurav.CarPoolingApplication.Service.UserEntityService.AuthService;
import com.gaurav.CarPoolingApplication.Service.UserEntityService.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/public")
public class PublicController {
    private final AuthService authService;
    private final UserService userService;
    public PublicController(
            AuthService authService,
            UserService userService) {
        this.userService = userService;
        this.authService = authService;
    }
    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse userRegistrationResponse = this.userService.registerUser(userRegistrationRequest);
        return new ResponseEntity<>(Map.of(
                "response", userRegistrationResponse
        ), HttpStatus.OK);
    }
    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOTP(@RequestParam("email") String email) {
        String message = this.authService.requestOTP(email);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam("email") String email,
            @RequestParam("OTP") String otp,
            @RequestParam("new password") String newPassword) {
        String message = this.authService.forgotPassword(email, otp, newPassword);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
}
