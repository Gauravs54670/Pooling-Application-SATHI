package com.gaurav.CarPoolingApplication.Controller;

import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserSignInRequest;
import com.gaurav.CarPoolingApplication.JWT.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Slf4j @RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    public AuthenticationController(
            JWTUtils jwtUtils,
            AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserSignInRequest userSignInRequest) {
//        authenticating the user by username and password and capturing in the authentication object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userSignInRequest.getCredential(), userSignInRequest.getPassword())
        );
        log.info("Authentication Completed.");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        assert userDetails != null;
        String jwtToken = this.jwtUtils.generateJwtToken(userDetails);
        return new ResponseEntity<>(Map.of(
                "message", "Token creation successful",
                "response", jwtToken
        ), HttpStatus.OK);
    }
}
