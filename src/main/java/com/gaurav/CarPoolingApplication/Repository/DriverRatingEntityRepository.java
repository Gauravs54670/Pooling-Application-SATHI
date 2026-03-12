package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverRatingEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRatingEntityRepository extends JpaRepository<DriverRatingEntity, Long> {
    boolean existsByRideAndPassenger(RideEntity ride, UserEntity passenger);
}
