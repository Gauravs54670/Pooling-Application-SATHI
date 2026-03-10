package com.gaurav.CarPoolingApplication.DTO.UserDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class UserRegistrationResponse {
    private Long userId;
    private String email;
    private String phoneNumber;
    private String message;
}
