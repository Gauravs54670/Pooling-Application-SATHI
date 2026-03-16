package com.gaurav.CarPoolingApplication.Repository;

import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.GPSRideTrackingEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GPSRideTrackingRepository extends JpaRepository<GPSRideTrackingEntity, Long> {
    List<GPSRideTrackingEntity> findByRideOrderByRecordedAtAsc(RideEntity ride);
    void deleteByRide(RideEntity ride);
}
