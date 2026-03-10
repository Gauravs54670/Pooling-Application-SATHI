package com.gaurav.CarPoolingApplication.DTO.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class UpdateProfileRequest {
    private String email;
    private String phoneNumber;
    private String userFullName;
}
