package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findByEmailOrPhoneNumber(String email, String phoneNumber);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO(
                u.userId,
                u.email,
                u.phoneNumber,
                u.userFullName,
                u.userAccountStatus,
                u.accountCreatedAt,
                u.accountUpdatedAt
            )
            FROM UserEntity u
            WHERE u.email = :email
            """)
    Optional<UserProfileDTO> findUserProfileByEmail(@Param("email") String email);
    @Query("""
            SELECT new com.gaurav.CarPoolingApplication.DTO.UserDTO.UserProfileDTO(
                u.userId,
                u.email,
                u.phoneNumber,
                u.userFullName,
                u.userAccountStatus,
                u.accountCreatedAt,
                u.accountUpdatedAt
            )
            FROM UserEntity u
            WHERE u.phoneNumber = :phoneNumber
            """)
    Optional<UserProfileDTO> findUserProfileByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
