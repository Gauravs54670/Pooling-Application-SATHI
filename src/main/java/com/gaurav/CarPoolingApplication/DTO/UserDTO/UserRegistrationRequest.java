package com.gaurav.CarPoolingApplication.DTO.UserDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class UserRegistrationRequest {
    private String email;
    private String phoneNumber;
    private String userFullName;
    private String password;
}
