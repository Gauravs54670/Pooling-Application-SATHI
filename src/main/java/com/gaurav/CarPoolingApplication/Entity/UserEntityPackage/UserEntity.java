package com.gaurav.CarPoolingApplication.Entity.UserEntityPackage;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
@Entity @Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String userFullName;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAccountStatus userAccountStatus;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role", length = 20)
    private Set<UserRole> userRoles;
    private String otpColumn;
    private LocalDateTime otpExpirationTime;
    private LocalDateTime accountCreatedAt;
    private LocalDateTime accountUpdatedAt;
}
