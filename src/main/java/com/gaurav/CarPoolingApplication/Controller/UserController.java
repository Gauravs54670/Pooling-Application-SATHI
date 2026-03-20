package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileRequest;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileResponse;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.ChangePasswordRequestDTO;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.UpdateProfileRequest;
import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserRole;
import com.gaurav.CarPoolingApplication.Service.DriverEntityService.DriverService;
import com.gaurav.CarPoolingApplication.Service.UserEntityService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController @RequestMapping("/user")
public class UserController {
    private final DriverService driverService;
    private final UserService userService;
    public UserController(
            DriverService driverService,
            UserService userService) {
        this.userService = userService;
        this.driverService = driverService;
    }
//    get user's profile
    @GetMapping("/myProfile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        String credential = authentication.getName();
        UserProfileDTO profileDTO = this.userService.findMyProfile(credential);
        return new ResponseEntity<>(Map.of(
                "message", "Profile fetched successfully",
                "response", profileDTO
        ), HttpStatus.OK);
    }
//    update the user's profile
    @PutMapping("/update-myProfile")
    public ResponseEntity<?> updateMyProfile(
            @RequestBody UpdateProfileRequest updateProfileRequest,
            Authentication authentication) {
        String credential = authentication.getName();
        UserProfileDTO profileDTO = this.userService.updateMyProfile(credential, updateProfileRequest);
        return new ResponseEntity<>(Map.of(
                "message", "Profile Updated",
                "response", profileDTO
        ),HttpStatus.OK);
    }
//     get user's role
    @GetMapping("/get-roles")
    public ResponseEntity<?> getRoles(Authentication authentication) {
        String credential = authentication.getName();
        Set<UserRole> userRoles = this.userService.getMyRoles(credential);
        return new ResponseEntity<>(Map.of(
                "message", "User's Role",
                "response", userRoles
        ),HttpStatus.OK);
    }
//    deactivate account
    @PutMapping("/deactivate-account")
    public ResponseEntity<?> deactivateAccount(Authentication authentication) {
        String credential = authentication.getName();
        String message = this.userService.deactivateMyAccount(credential);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    activate account
    @PutMapping("/activate-account")
    public ResponseEntity<?> activateAccount(Authentication authentication) {
        String credential = authentication.getName();
        String message = this.userService.activateMyAccount(credential);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    upgrade the user's profile to driver's profile
    @PostMapping(value = "/register-driver",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerDriver(
            @RequestPart("media") MultipartFile multipartFile,
            @RequestPart("driverProfileRequest") String driverProfileRequest,
            Authentication authentication) {
        String credential = authentication.getName();
        ObjectMapper mapper = new ObjectMapper();
        DriverProfileRequest request = mapper.readValue(driverProfileRequest,DriverProfileRequest.class);
        DriverProfileResponse driverProfileResponse = this.driverService.registerDriver(
                credential,
                multipartFile,
                request
        );
        return new ResponseEntity<>(Map.of(
                "message", "Driver Registered successfully",
                "response", driverProfileResponse
        ),HttpStatus.OK);
    }
}
