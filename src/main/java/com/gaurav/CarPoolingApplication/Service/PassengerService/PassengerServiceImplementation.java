package com.gaurav.CarPoolingApplication.Service.PassengerService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRatingClass;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.MyRideRequests;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequest;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerRideRequestResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.AvailableRidesDTO;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideSearchRequestDTO;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverProfileEntity;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.DriverRatingEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.PassengerRideRequestEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserAccountStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import com.gaurav.CarPoolingApplication.Exception.ResourceNotFoundException;
import com.gaurav.CarPoolingApplication.Exception.UserNotFoundException;
import com.gaurav.CarPoolingApplication.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class PassengerServiceImplementation implements PassengerService{

    private final DriverRatingEntityRepository driverRatingEntityRepository;
    private final PassengerRideRequestRepository passengerRideRequestRepository;
    private final UserEntityRepository userEntityRepository;
    private final DriverEntityRepository driverEntityRepository;
    private final RideEntityRepository rideEntityRepository;
    public PassengerServiceImplementation(
            DriverRatingEntityRepository driverRatingEntityRepository,
            PassengerRideRequestRepository passengerRideRequestRepository,
            RideEntityRepository rideEntityRepository,
            UserEntityRepository userEntityRepository,
            DriverEntityRepository driverEntityRepository) {
        this.passengerRideRequestRepository = passengerRideRequestRepository;
        this.driverEntityRepository = driverEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.rideEntityRepository = rideEntityRepository;
        this.driverRatingEntityRepository = driverRatingEntityRepository;
    }
//    get available rides when passenger opens ride
    @Override
    public List<AvailableRidesDTO> getAvailableRides(String email, RideSearchRequestDTO rideSearchRequestDTO) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        List<RideEntity> totalRides = this.rideEntityRepository.findAvailableRides();
        List<AvailableRidesDTO> allCompatibleRides = new ArrayList<>();
        for(RideEntity r : totalRides) {
            double sourceDistance = calculateDistance(
                    rideSearchRequestDTO.getPassengerSourceLat(),
                    rideSearchRequestDTO.getPassengerSourceLong(),
                    r.getSourceLat(),
                    r.getSourceLong());
            double destinationDistance = calculateDistance(
                    rideSearchRequestDTO.getPassengerDestinationLat(),
                    rideSearchRequestDTO.getPassengerDestinationLong(),
                    r.getDestinationLat(),
                    r.getDestinationLong());
            if(sourceDistance <= 1.5 && destinationDistance <= 1.5)
                allCompatibleRides.add(mapToAvailableRideDTO(r));
        }
        return allCompatibleRides;
    }
//    request a ride from passenger end
    @Override @Transactional
    public PassengerRideRequestResponse requestRide(
            String email, PassengerRideRequest passengerRideRequest) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        Optional<PassengerRideRequestEntity> requestEntity =
                this.passengerRideRequestRepository
                    .findByPassengerAndRideCode(passenger, passengerRideRequest.getRideCode());
        if(requestEntity.isPresent())
            throw new
                    AccessDeniedException("You have already requested for this ride." +
                    " Please wait till driver accept your ride.");
        RideEntity ride = this.rideEntityRepository.findByRideCode(passengerRideRequest.getRideCode())
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found."));
        if(ride.getDriverProfileEntity()
                .getUser()
                .getUserId()
                .equals(passenger.getUserId()))
            throw new RuntimeException("Driver cannot request their own ride.");
        if(ride.getRideStatus().equals(RideStatus.CANCELLED) || ride.isRideDeleted())
            throw new RuntimeException("Ride is cancelled.");
        if(ride.getAvailableSeats() < passengerRideRequest.getRequiredSeats())
            throw new RuntimeException("Seats are full.");
        if(ride.getDepartureTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Ride is expired.");
        if(ride.getRideStatus() != RideStatus.ACTIVE)
            throw new RuntimeException("Rider not accepting requests");
        PassengerRideRequestEntity rideRequestEntity = PassengerRideRequestEntity
                .builder()
                .rideCode(ride.getRideCode())
                .ride(ride)
                .passenger(passenger)
                .requestedSeats(passengerRideRequest.getRequiredSeats())
                .sourceLat(passengerRideRequest.getSourceLat())
                .sourceLong(passengerRideRequest.getSourceLong())
                .sourceAddress(passengerRideRequest.getSourceAddress())
                .destinationLat(passengerRideRequest.getDestinationLat())
                .destinationLong(passengerRideRequest.getDestinationLong())
                .destinationAddress(passengerRideRequest.getDestinationAddress())
                .rideRequestStatus(RideRequestStatus.PENDING)
                .rideRequestedAt(LocalDateTime.now())
                .build();
        rideRequestEntity = this.passengerRideRequestRepository.save(rideRequestEntity);
        return PassengerRideRequestResponse.builder()
                .requestId(rideRequestEntity.getRequestId())
                .rideCode(rideRequestEntity.getRideCode())
                .passengerName(passenger.getUserFullName())
                .driverName(ride.getDriverProfileEntity().getUser().getUserFullName())
                .requestStatus(rideRequestEntity.getRideRequestStatus().toString())
                .requestedSeats(passengerRideRequest.getRequiredSeats())
                .requestCreatedAt(rideRequestEntity.getRideRequestedAt())
                .sourceAddress(ride.getSourceAddress())
                .destinationAddress(ride.getDestinationAddress())
                .departureTime(ride.getDepartureTime())
                .build();
    }
//    passenger gets ride-sharing decision
    @Override
    public PassengerRideRequestDecisionResponse getRideSharingDecision(
            String email, Long requestId, String rideCode) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        PassengerRideRequestEntity passengerRideRequest =
                this.passengerRideRequestRepository.findById(requestId)
                        .orElseThrow(() -> new ResourceNotFoundException("Ride request not found."));
        if(!passengerRideRequest.getRideCode().equals(rideCode))
            throw new AccessDeniedException("Invalid ride request.");
        if(!passengerRideRequest.getPassenger().getUserId().equals(passenger.getUserId()))
            throw new AccessDeniedException("You are not requested this ride.");
        return this.passengerRideRequestRepository.getDecisionResponse(requestId,rideCode);
    }

    @Override
    public List<MyRideRequests> getMyRideRequestStatus(String email, LocalDate date) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        return this.passengerRideRequestRepository
                .getMyRidesRequestStatus(passenger.getUserId(), date);
    }
//    needed to completed
//    rate driver
    @Override @Transactional
    public DriverRatingClass rateDriver(
            String email, Integer rating, String comment, String rideCode) {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        RideEntity ride = this.rideEntityRepository.findByRideCode(rideCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found."));
        if(ride.getDriverProfileEntity().getUser().getUserId().equals(passenger.getUserId()))
            throw new RuntimeException("You can't rate yourself.");
        DriverProfileEntity driverProfile = ride.getDriverProfileEntity();
        if(driverProfile.getUser().getUserAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("Driver's account is SUSPENDED.");
        if(driverProfile.getUser().getUserAccountStatus().equals(UserAccountStatus.DEACTIVATED))
            throw new AccessDeniedException("Driver's account is DEACTIVATED.");
        boolean isAlreadyRated = this.driverRatingEntityRepository.existsByRideAndPassenger(ride, passenger);
        if(isAlreadyRated)
            throw new AccessDeniedException("You already reviewed this ride.");
        PassengerRideRequestEntity rideRequestEntity = this.passengerRideRequestRepository
                .findByPassengerAndRideCode(passenger,rideCode)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found."));
        if(!rideRequestEntity.getRide().equals(ride))
            throw new RuntimeException("Invalid ride request.");
        if(ride.getRideStatus() != (RideStatus.COMPLETED))
            throw new AccessDeniedException("You can't review driver before ride is complete.");
        DriverRatingEntity driverRating = DriverRatingEntity.builder()
                .driverProfile(driverProfile)
                .passenger(passenger)
                .ride(ride)
                .rating(rating)
                .comment(comment)
                .build();
        if (driverProfile.getTotalReviewCount() == null || driverProfile.getTotalReviewCount() == 0) {
            driverProfile.setTotalDriverRating(rating);
            driverProfile.setTotalReviewCount(1);
            driverProfile.setAverageRatingOfDriver((double) rating);
        }
        else {
            int oldCount = driverProfile.getTotalReviewCount();
            double oldAvg = driverProfile.getAverageRatingOfDriver();
            int newCount = oldCount + 1;
            double newAvg = ((oldAvg * oldCount) + rating) / newCount;
            driverProfile.setTotalReviewCount(newCount);
            driverProfile.setAverageRatingOfDriver(newAvg);
            driverProfile.setTotalDriverRating(
                    driverProfile.getTotalDriverRating() + rating
            );
        }
        driverRating.setReviewedAt(LocalDateTime.now());
        this.driverRatingEntityRepository.save(driverRating);
        this.driverEntityRepository.save(driverProfile);
        return DriverRatingClass.builder()
                .rating(rating)
                .comment(comment)
                .rideCode(rideCode)
                .build();
    }
    //    helper methods
//    validate the passenger's account
    private void validatePassengerAccount(UserEntity user) {
        if(user.getUserAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("Account Suspended.");
        if(user.getUserAccountStatus().equals(UserAccountStatus.DEACTIVATED))
            throw new AccessDeniedException("Account DEACTIVATED.");
    }
    //    calculate distance between source and destination
    private double calculateDistance(
            Double sourceLat,
            Double sourceLong,
            Double destinationLat,
            Double destinationLong) {
        final int EARTH_RADIUS = 6371; // km
        double latDistance = Math.toRadians(destinationLat - sourceLat);
        double lonDistance = Math.toRadians(destinationLong - sourceLong);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(sourceLat))
                * Math.cos(Math.toRadians(destinationLat))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(EARTH_RADIUS * c * 100.0) / 100.0;
    }
//    map ride entity to available ride dto
    private AvailableRidesDTO mapToAvailableRideDTO(RideEntity ride) {
        return AvailableRidesDTO.builder()
                .rideCode(ride.getRideCode())
                .driverName(ride.getDriverProfileEntity().getUser().getUserFullName())
                .driverRating(ride.getDriverProfileEntity().getAverageRatingOfDriver())
                .driverSourceLocation(ride.getSourceAddress())
                .driverDestinationLocation(ride.getDestinationAddress())
                .rideDepartureTime(ride.getDepartureTime())
                .totalSeats(ride.getTotalSeats())
                .totalAvailableSeats(ride.getAvailableSeats())
                .pricePerKm(ride.getPricePerKm())
                .estimatedFare(ride.getEstimatedTotalFare())
                .vehicleModel(ride.getDriverProfileEntity().getVehicleModel())
                .vehicleCategory(ride.getVehicleCategory().name())
                .vehicleClass(ride.getVehicleClass().name())
                .rideStatus(ride.getRideStatus().name())
                .build();
    }
}
