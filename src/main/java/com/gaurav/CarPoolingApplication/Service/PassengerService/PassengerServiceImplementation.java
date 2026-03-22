package com.gaurav.CarPoolingApplication.Service.PassengerService;

import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRatingClass;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.*;
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
import com.gaurav.CarPoolingApplication.Exception.InvalidRideStateException;
import com.gaurav.CarPoolingApplication.Exception.ResourceNotFoundException;
import com.gaurav.CarPoolingApplication.Exception.UserNotFoundException;
import com.gaurav.CarPoolingApplication.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
        // 1.5km in degrees ≈ 0.015 (1 degree ≈ 111km)
        double radius = 1.5 / 111.0;
        List<RideEntity> totalRides = this.rideEntityRepository.findAvailableRides(
                rideSearchRequestDTO.getPassengerSourceLat(),
                rideSearchRequestDTO.getPassengerSourceLong(),
                rideSearchRequestDTO.getPassengerDestinationLat(),
                rideSearchRequestDTO.getPassengerDestinationLong(),
                radius
        );
        return totalRides.stream()
                .map(this::mapToAvailableRideDTO)
                .toList();
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
        if(ride.isRideDeleted())
            throw new InvalidRideStateException("Ride is cancelled.");
        if(ride.getRideStatus() != RideStatus.ACTIVE)
            throw new InvalidRideStateException("Ride is not accepting requests.");
        if(ride.getDepartureTime().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Ride has already expired.");
        if(ride.getDriverProfileEntity().getUser().getUserId().equals(passenger.getUserId()))
            throw new AccessDeniedException("Driver cannot request their own ride.");
        if(passengerRideRequest.getRequiredSeats() <= 0)
            throw new IllegalArgumentException("Requested seats must be greater than zero.");
        if(ride.getAvailableSeats() < passengerRideRequest.getRequiredSeats())
            throw new IllegalStateException("Not enough seats available.");
        PassengerRideRequestEntity rideRequestEntity = PassengerRideRequestEntity
                .builder()
                .ride(ride)
                .rideCode(passengerRideRequest.getRideCode())
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
                .passengerName(passenger.getUserFullName())
                .driverName(ride.getDriverProfileEntity().getUser().getUserFullName())
                .requestStatus(rideRequestEntity.getRideRequestStatus().toString())
                .requestedSeats(passengerRideRequest.getRequiredSeats())
                .requestCreatedAt(rideRequestEntity.getRideRequestedAt())
                .sourceAddress(passengerRideRequest.getSourceAddress())
                .destinationAddress(passengerRideRequest.getDestinationAddress())
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
//    fetch the accepted ride request status
    @Override
    public MyRideRequests getMyRideRequestStatus(String email, Long requestId) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        PassengerRideRequestEntity passengerRideRequest = this
                .passengerRideRequestRepository.findByPassengerAndRequestId(passenger, requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger Request not found."));
        return this.passengerRideRequestRepository.ridesRequestedByDriver(passengerRideRequest.getRequestId(), RideRequestStatus.ACCEPTED);
    }
//    exit ride by passenger
    @Override @Transactional
    public PassengerExitRideResponseDTO existRide(
            String email,
            PassengerRideExistRequestDTO passengerRideExistRequestDTO) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        PassengerRideRequestEntity passengerRideRequest = this.passengerRideRequestRepository
                .findByPassengerAndRequestId(passenger, passengerRideExistRequestDTO.getRideRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Ride request not found. " +
                        "Please check the request Id or ride code."));
        if(passengerRideRequest.getRideRequestStatus() == RideRequestStatus.COMPLETED)
            throw new InvalidRideStateException("Ride sharing is already completed.");
        if(passengerRideRequest.getRideRequestStatus() != RideRequestStatus.ACCEPTED)
            throw new InvalidRideStateException("Only ACCEPTED passengers can exit a ride.");
        RideEntity ride = passengerRideRequest.getRide();
        if(ride.getRideStatus() != RideStatus.STARTED)
            throw new InvalidRideStateException("Can only exit a ride which is running");
        double distanceTravelledByPassenger = calculateDistance(
                passengerRideRequest.getSourceLat(),
                passengerRideRequest.getSourceLong(),
                passengerRideExistRequestDTO.getActualDestinationLat(),
                passengerRideExistRequestDTO.getActualDestinationLong()
        );
        if(distanceTravelledByPassenger == 0)
            throw new IllegalStateException("Cannot exit at boarding location.");
        BigDecimal passengerRideSharingFare = ride.getPricePerKm()
                .multiply(BigDecimal.valueOf(distanceTravelledByPassenger))
                .multiply(BigDecimal.valueOf(passengerRideRequest.getRequestedSeats()))
                .setScale(2, RoundingMode.HALF_UP);
        passengerRideRequest.setPassengerActualDistance(
                BigDecimal.valueOf(distanceTravelledByPassenger)
                        .setScale(2, RoundingMode.HALF_UP));
        passengerRideRequest.setPassengerActualFare(passengerRideSharingFare);
        passengerRideRequest.setRideRequestStatus(RideRequestStatus.COMPLETED);
        passengerRideRequest.setRideExitedAt(LocalDateTime.now());
        ride.setAvailableSeats(ride.getAvailableSeats() + passengerRideRequest.getRequestedSeats());
//        payment here
        this.passengerRideRequestRepository.save(passengerRideRequest);
        this.rideEntityRepository.save(ride);
        return PassengerExitRideResponseDTO.builder()
                .requestId(passengerRideRequest.getRequestId())
                .rideCode(ride.getRideCode())
                .boardingAddress(passengerRideRequest.getSourceAddress())
                .exitAddress(passengerRideExistRequestDTO.getActualDestinationAddress())
                .pricePerKm(ride.getPricePerKm())
                .actualDistance(passengerRideRequest.getPassengerActualDistance())
                .actualFare(passengerRideRequest.getPassengerActualFare())
                .seatsReleased(passengerRideRequest.getRequestedSeats())
                .exitedAt(passengerRideRequest.getRideExitedAt())
                .build();
    }
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
//    get the history of the rides
    @Override
    public List<PassengerRideHistoryDTO> getRideHistory(String email) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        return this
                .passengerRideRequestRepository
                .getPassengerRideHistory(passenger.getUserId());
    }
//    cancel the ride request
    @Override @Transactional
    public String cancelRideRequest(String email, Long requestId) {
        UserEntity passenger = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validatePassengerAccount(passenger);
        PassengerRideRequestEntity rideRequestEntity = this.passengerRideRequestRepository
                .findByPassengerAndRequestId(passenger, requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride request not found."));
        if(rideRequestEntity.getRideRequestStatus() == RideRequestStatus.CANCELLED)
            throw new InvalidRideStateException("Ride is already cancelled.");
        if(rideRequestEntity.getRideRequestStatus() == RideRequestStatus.COMPLETED)
            throw new InvalidRideStateException("Ride is already completed. " +
                    "Only in-completed rides can be cancelled.");
        if(rideRequestEntity.getRideRequestStatus() == RideRequestStatus.REJECTED)
            throw new IllegalStateException("Ride request is rejected by driver.");
        RideEntity ride = rideRequestEntity.getRide();
        if(rideRequestEntity.getRideRequestStatus() == RideRequestStatus.ACCEPTED
                && ride.getRideStatus() == RideStatus.STARTED) {
            rideRequestEntity.setRideRequestStatus(RideRequestStatus.NOT_BOARDED);
            // release seats back
            ride.setAvailableSeats(ride.getAvailableSeats() + rideRequestEntity.getRequestedSeats());
            if(ride.getRideStatus() == RideStatus.FULL)
                ride.setRideStatus(RideStatus.STARTED);
            this.rideEntityRepository.save(ride);
            this.passengerRideRequestRepository.save(rideRequestEntity);
            // TODO: notify driver — passenger did not board
            return "No-show reported. Your request has been closed.";
        }
        // only release seats if request was ACCEPTED — PENDING never reserved seats
        if(rideRequestEntity.getRideRequestStatus() == RideRequestStatus.ACCEPTED) {
            ride.setAvailableSeats(ride.getAvailableSeats() + rideRequestEntity.getRequestedSeats());
            if(ride.getRideStatus() == RideStatus.FULL)
                ride.setRideStatus(RideStatus.ACTIVE);
            this.rideEntityRepository.save(ride);
        }
        rideRequestEntity.setRideRequestStatus(RideRequestStatus.CANCELLED);
        this.passengerRideRequestRepository.save(rideRequestEntity);
        return "Ride request cancelled successfully.";
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
