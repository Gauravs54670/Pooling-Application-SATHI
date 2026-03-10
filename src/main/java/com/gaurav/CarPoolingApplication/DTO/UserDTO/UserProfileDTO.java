package com.gaurav.CarPoolingApplication.DTO.UserDTO;

import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserAccountStatus;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor @Getter @Setter @Builder
public class UserProfileDTO {
    private Long userId;
    private String email;
    private String phoneNumber;
    private String userFullName;
    private UserAccountStatus userAccountStatus;
    private LocalDateTime accountCreatedAt;
    private LocalDateTime accountUpdatedAt;
    public UserProfileDTO(
            Long userId,
            String email,
            String phoneNumber,
            String userFullName,
            UserAccountStatus userAccountStatus,
            LocalDateTime accountCreatedAt,
            LocalDateTime accountUpdatedAt) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userFullName = userFullName;
        this.userAccountStatus = userAccountStatus;
        this.accountCreatedAt = accountCreatedAt;
        this.accountUpdatedAt = accountUpdatedAt;
    }
}
