package com.gaurav.CarPoolingApplication.Service.DriverEntityService;

import com.cloudinary.Cloudinary;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.BookingResponse;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileDTO;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverProfileUpdateRequest;
import com.gaurav.CarPoolingApplication.DTO.PassengerDTO.PassengerBookingRequest;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideRequest;
import com.gaurav.CarPoolingApplication.DTO.DriverDTO.DriverRideRequestDecisionResponse;
import com.gaurav.CarPoolingApplication.DTO.RideDTO.RideResponse;
import com.gaurav.CarPoolingApplication.Entity.DriverEntityPackage.*;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.PassengerRideRequestEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideEntity;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideRequestStatus;
import com.gaurav.CarPoolingApplication.Entity.RideEntityPackage.RideStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserAccountStatus;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserRole;
import com.gaurav.CarPoolingApplication.Exception.InvalidRideStateException;
import com.gaurav.CarPoolingApplication.Exception.ResourceNotFoundException;
import com.gaurav.CarPoolingApplication.Exception.UserNotFoundException;
import com.gaurav.CarPoolingApplication.Repository.DriverEntityRepository;
import com.gaurav.CarPoolingApplication.Repository.PassengerRideRequestRepository;
import com.gaurav.CarPoolingApplication.Repository.RideEntityRepository;
import com.gaurav.CarPoolingApplication.Repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DriverServiceImplementation implements DriverService{

    private final RideEntityRepository rideEntityRepository;
    private final Cloudinary cloudinary;
    private final UserEntityRepository userEntityRepository;
    private final DriverEntityRepository driverEntityRepository;
    private final PassengerRideRequestRepository passengerRideRequestRepository;
    public DriverServiceImplementation(
            PassengerRideRequestRepository passengerRideRequestRepository,
            RideEntityRepository rideEntityRepository,
            Cloudinary cloudinary,
            UserEntityRepository userEntityRepository,
            DriverEntityRepository driverEntityRepository) {
        this.passengerRideRequestRepository = passengerRideRequestRepository;
        this.driverEntityRepository = driverEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.cloudinary = cloudinary;
        this.rideEntityRepository = rideEntityRepository;
    }
    @Override
    public DriverProfileDTO getMyDriverProfile(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found"));
        validateUserAccount(user);
        if(!user.getUserRoles().contains(UserRole.DRIVER_ROLE))
            throw new AccessDeniedException("Access Denied. First register yourself as driver.");
        return this.driverEntityRepository.findMyProfile(email)
                .orElseThrow(() -> new UserNotFoundException("Profile not found."));
    }
//    update driver profile
    @Override
    public DriverProfileDTO updateDriverProfile(String email, DriverProfileUpdateRequest request) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        DriverProfileEntity driverProfileEntity = this.driverEntityRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Driver profile not found."));
        if(request.getDriverLicenseNumber() != null && !request.getDriverLicenseNumber().isEmpty())
            driverProfileEntity.setDriverLicenseNumber(request.getDriverLicenseNumber());
        if(request.getLicenseExpirationDate() != null) {
            if(request.getLicenseExpirationDate().isBefore(LocalDate.now()))
                throw new IllegalArgumentException("Licence already expired.");
            driverProfileEntity.setLicenseExpirationDate(request.getLicenseExpirationDate());
        }
        if(request.getVehicleModel() != null && !request.getVehicleModel().isEmpty())
            driverProfileEntity.setVehicleModel(request.getVehicleModel());
        if(request.getVehicleNumber() != null && !request.getVehicleNumber().isEmpty()) {
            Optional<DriverProfileEntity> profile = this.driverEntityRepository.findByVehicleNumber(request.getVehicleNumber());
            if(profile.isPresent() && !profile.get().getDriverId().equals(driverProfileEntity.getDriverId()))
                throw new IllegalArgumentException("Vehicle number already registered.");
            driverProfileEntity.setVehicleNumber(request.getVehicleNumber());
        }
        if(request.getVehicleSeatCapacity() != null && request.getVehicleSeatCapacity() > 0)
            driverProfileEntity.setVehicleSeatCapacity(request.getVehicleSeatCapacity());
        if(request.getVehicleCategory() != null && !request.getVehicleCategory().isEmpty()) {
            VehicleCategory vehicleCategory;
            try {
                vehicleCategory = VehicleCategory
                        .valueOf(request.getVehicleCategory().trim().toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                String allowedValues = Arrays.stream(VehicleCategory.values())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));
                throw new IllegalArgumentException(
                        "Invalid Vehicle Category. Allowed values are: " + allowedValues
                );
            }
            driverProfileEntity.setVehicleCategory(vehicleCategory);
        }
        if(request.getVehicleClass() != null && !request.getVehicleClass().isEmpty()) {
            VehicleClass vehicleClass;
            try {
                vehicleClass = VehicleClass
                        .valueOf(request.getVehicleClass().trim().toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                String allowedValues = Arrays.stream(VehicleClass.values())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));
                throw new IllegalArgumentException(
                        "Invalid Vehicle Category. Allowed values are: " + allowedValues
                );
            }
            driverProfileEntity.setVehicleClass(vehicleClass);
        }
        driverProfileEntity.setAccountUpdatedAt(LocalDateTime.now());
        this.driverEntityRepository.save(driverProfileEntity);
        return DriverProfileDTO.builder()
                .driverId(driverProfileEntity.getDriverId())
                .email(driverProfileEntity.getUser().getEmail())
                .phoneNumber(driverProfileEntity.getUser().getPhoneNumber())
                .userFullName(driverProfileEntity.getUser().getUserFullName())
                .driverProfileUrl(driverProfileEntity.getDriverProfileUrl())
                .driverLicenseNumber(driverProfileEntity.getDriverLicenseNumber())
                .licenseExpirationDate(driverProfileEntity.getLicenseExpirationDate())
                .vehicleModel(driverProfileEntity.getVehicleModel())
                .vehicleNumber(driverProfileEntity.getVehicleNumber())
                .vehicleSeatCapacity(driverProfileEntity.getVehicleSeatCapacity())
                .vehicleCategory(driverProfileEntity.getVehicleCategory())
                .vehicleClass(driverProfileEntity.getVehicleClass())
                .driverVerificationStatus(driverProfileEntity.getDriverVerificationStatus())
                .driverAvailabilityStatus(driverProfileEntity.getDriverAvailabilityStatus())
                .totalCompletedRides(driverProfileEntity.getTotalCompletedRides())
                .totalCancelledRides(driverProfileEntity.getTotalCancelledRides())
                .averageRatingOfDriver(driverProfileEntity.getAverageRatingOfDriver())
                .totalReviewCount(driverProfileEntity.getTotalReviewCount())
                .build();
    }
//    update photo
    @Override
    public String updatePhoto(String email, MultipartFile file) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        DriverProfileEntity driverProfileEntity = this.driverEntityRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        String profileUrl;
        try {
            log.info("Profile update.");
            Map<String, Object> uploadOption = new HashMap<>();
            uploadOption.put("resource_type", "image");
            Map result = cloudinary.uploader().upload(file.getBytes(), uploadOption);
            profileUrl = (String) result.getOrDefault("secure_url", null);
            if(profileUrl == null)
                throw new RuntimeException("Media upload failed: Cloudinary response incomplete.");
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        driverProfileEntity.setDriverProfileUrl(profileUrl);
        driverProfileEntity.setAccountUpdatedAt(LocalDateTime.now());
        this.driverEntityRepository.save(driverProfileEntity);
        return profileUrl;
    }
//    post a ride by driver
    @Override @Transactional
    public RideResponse postRide(String email, RideRequest request) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        DriverProfileEntity driverProfile = this.driverEntityRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        if(!driverProfile.getDriverVerificationStatus().equals(DriverVerificationStatus.APPROVED))
            throw new AccessDeniedException("Driver not verified yet.");
        if(!driverProfile.getDriverAvailabilityStatus().equals(DriverAvailabilityStatus.ONLINE))
            throw new IllegalStateException("Driver must be ONLINE to post ride.");
        if(request.getAvailableSeats() <= 0)
            throw new IllegalArgumentException("No seats available.");
        if(request.getDepartureTime().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Please recheck the departure time. Departure time is in past.");
        if(request.getAvailableSeats() > driverProfile.getVehicleSeatCapacity())
            throw new IllegalArgumentException("Requested seats exceed vehicle capacity.");
        double distance = calculateDistance(
                request.getSourceLat(),
                request.getSourceLong(),
                request.getDestinationLat(),
                request.getDestinationLong());
        BigDecimal pricePerKm = calculatePricePerKm(
                driverProfile.getVehicleCategory(),driverProfile.getVehicleClass());
        BigDecimal totalFare = pricePerKm
                .multiply(BigDecimal.valueOf(distance))
                .setScale(2, RoundingMode.HALF_UP);
        String rideCode = generateRideCode();
        RideEntity ride = RideEntity.builder()
                .driverProfileEntity(driverProfile)
                .sourceLat(request.getSourceLat())
                .sourceLong(request.getSourceLong())
                .sourceAddress(request.getSourceAddress())
                .destinationLat(request.getDestinationLat())
                .destinationLong(request.getDestinationLong())
                .destinationAddress(request.getDestinationAddress())
                .vehicleClass(driverProfile.getVehicleClass())
                .vehicleCategory(driverProfile.getVehicleCategory())
                .estimatedTotalDistance(BigDecimal.valueOf(distance))
                .pricePerKm(pricePerKm)
                .estimatedTotalFare(totalFare)
                .actualTotalDistance(BigDecimal.valueOf(distance))
                .actualTotalFare(totalFare)
                .totalSeats(driverProfile.getVehicleSeatCapacity())
                .availableSeats(request.getAvailableSeats())
                .rideStatus(RideStatus.ACTIVE)
                .departureTime(request.getDepartureTime())
                .rideCreatedAt(LocalDateTime.now())
                .rideUpdatedAt(LocalDateTime.now())
                .rideCode(rideCode)
                .build();
        this.rideEntityRepository.save(ride);
        return RideResponse.builder()
                .rideCode(rideCode)
                .sourceLat(ride.getSourceLat())
                .sourceLong(ride.getSourceLong())
                .sourceAddress(ride.getSourceAddress())
                .destinationLat(ride.getDestinationLat())
                .destinationLong(ride.getDestinationLong())
                .destinationAddress(ride.getDestinationAddress())
                .departureTime(ride.getDepartureTime())
                .totalSeats(ride.getTotalSeats())
                .availableSeats(ride.getAvailableSeats())
                .estimatedTotalDistance(ride.getEstimatedTotalDistance())
                .pricePerKm(ride.getPricePerKm())
                .estimatedTotalFare(ride.getEstimatedTotalFare())
                .actualTotalFare(ride.getActualTotalFare())
                .actualTotalDistance(ride.getActualTotalDistance())
                .rideCreatedAt(ride.getRideCreatedAt())
                .build();
    }
//    change the availability status
    @Override
    public String changeAvailabilityStatus(String email, String availabilityStatus) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        DriverProfileEntity driverProfileEntity = this.driverEntityRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        DriverAvailabilityStatus availability;
        try {
            availability = DriverAvailabilityStatus.valueOf(availabilityStatus.trim().toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage() + " " +
                    "Allowed values are ONLINE, OFFLINE");
        }
        driverProfileEntity.setDriverAvailabilityStatus(availability);
        this.driverEntityRepository.save(driverProfileEntity);
        return "Driver availability change to " + availabilityStatus;
    }
//    fetch all the posted ride by driver
    @Override
    public List<RideResponse> getMyPostedRides(String credential) {
        UserEntity user = this.userEntityRepository.findByEmail(credential)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        validateUserAccount(user);
        DriverProfileEntity driverProfileEntity = this.driverEntityRepository.findByUserEmail(credential)
                .orElseThrow(() -> new UserNotFoundException("Driver profile not found."));
        if(!driverProfileEntity.getDriverVerificationStatus().equals(DriverVerificationStatus.APPROVED))
            throw new AccessDeniedException("Access Denied. Only approved drivers can access this resource.");
        return this.rideEntityRepository.getDriverPostedRides(driverProfileEntity.getDriverId());
    }
//    cancel posted ride request
    @Override
    public String cancelRide(String credential, String rideCode) {
        UserEntity user = this.userEntityRepository.findByEmail(credential)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        DriverProfileEntity driverProfile = this.driverEntityRepository.findByUserEmail(credential)
                .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        RideEntity rideEntity = this.rideEntityRepository
                .findByDriverProfileEntity_DriverIdAndRideCode(driverProfile.getDriverId(), rideCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found."));
        if (!rideEntity.getRideStatus().equals(RideStatus.ACTIVE))
            throw new InvalidRideStateException(
                    "Only scheduled rides can be cancelled."
            );
        rideEntity.setRideStatus(RideStatus.CANCELLED);
        rideEntity.setRideDeleted(true);
        this.rideEntityRepository.save(rideEntity);
        return "Ride entity with ride code " + rideCode +" is cancelled.";
    }
//    let driver know requests are queued for ride-sharing
    @Override
    public List<BookingResponse> getRideBookings(String email, String rideCode) {
        UserEntity driver = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        DriverProfileEntity driverProfile = this.driverEntityRepository.findByUserEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("Driver Profile not found."));
        validateUserAccount(driver);
        RideEntity ride = this.rideEntityRepository.findByRideCode(rideCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found."));
        if(!ride.getDriverProfileEntity().getDriverId().equals(driverProfile.getDriverId()))
            throw new AccessDeniedException("You are not the owner of the ride.");
        List<BookingResponse> bookingResponses =
                this.passengerRideRequestRepository.getAllBookingRequests(rideCode,driverProfile.getDriverId());
        bookingResponses = bookingResponses.stream()
                .peek(response -> {
                    double distance = calculateDistance(
                            ride.getSourceLat(),
                            ride.getSourceLong(),
                            response.getPassengerSourceLat(),
                            response.getPassengerSourceLong()
                    );
                    response.setDistanceFromDriver(distance);
                })
                .sorted(Comparator.comparing(BookingResponse::getDistanceFromDriver))
                .toList();
        return bookingResponses;
    }
//    driver take decision over ride-sharing request by passenger
    @Override @Transactional
    public DriverRideRequestDecisionResponse rideSharingDecision(
            String email,
            String rideRequestDecision,
            PassengerBookingRequest passengerBookingRequest) {
        UserEntity driver = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(driver);
        RideRequestStatus rideRequestStatus;
        try {
            rideRequestStatus = RideRequestStatus.valueOf(rideRequestDecision.trim().toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("""
                    Invalid values.\
                     Allowed Values are\s
                        ACCEPTED,
                        REJECTED,
                        CANCELLED""");
        }
        RideEntity ride = this.rideEntityRepository.findByRideCode(passengerBookingRequest.getRideCode())
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found."));
//        validate ride belongs to driver
        if(!ride.getDriverProfileEntity().getUser().getUserId().equals(driver.getUserId()))
            throw new AccessDeniedException("You are not owner of this ride.");
        PassengerRideRequestEntity passengerRideRequest =
                this.passengerRideRequestRepository.findById(passengerBookingRequest.getRequestId())
                        .orElseThrow(() -> new ResourceNotFoundException("Ride request not found."));
//        verify that request belongs to the ride
        if(!passengerRideRequest.getRide().getRideId().equals(ride.getRideId()))
            throw new AccessDeniedException("Invalid ride request.");
        if(ride.getRideStatus().equals(RideStatus.CANCELLED)
                || !ride.getRideStatus().equals(RideStatus.ACTIVE)
                || ride.isRideDeleted())
            throw new RuntimeException("Ride is cancelled.");
        if(ride.getDepartureTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Ride is expired.");
        if(!passengerRideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING))
            throw new AccessDeniedException("Request has already been decided.");
        switch (rideRequestStatus) {
            case ACCEPTED -> {
                if (ride.getAvailableSeats() < passengerRideRequest.getRequestedSeats())
                    throw new RuntimeException("Seats are not available.");
                int remainingSeats = ride.getAvailableSeats() - passengerRideRequest.getRequestedSeats();
                if (remainingSeats <= 0)
                    ride.setRideStatus(RideStatus.FULL);
                passengerRideRequest.setRideRequestStatus(RideRequestStatus.ACCEPTED);
                ride.setAvailableSeats(Math.max(remainingSeats, 0));
                this.rideEntityRepository.save(ride);
            }
            case REJECTED ->
                    passengerRideRequest.setRideRequestStatus(RideRequestStatus.REJECTED);
            case CANCELLED ->
                    passengerRideRequest.setRideRequestStatus(RideRequestStatus.CANCELLED);
            default ->
                    throw new IllegalArgumentException("Invalid ride request decision.");
        }
        LocalDateTime decisionTime = LocalDateTime.now();
        passengerRideRequest.setDecisionTime(decisionTime);
        this.passengerRideRequestRepository.save(passengerRideRequest);
        return DriverRideRequestDecisionResponse.builder()
                .requestId(passengerRideRequest.getRequestId())
                .rideCode(ride.getRideCode())
                .passengerName(
                        rideRequestStatus == (RideRequestStatus.ACCEPTED) ?
                                passengerRideRequest.getPassenger().getUserFullName() : null)
                .passengerPhoneNumber(
                        rideRequestStatus == (RideRequestStatus.ACCEPTED) ?
                                passengerRideRequest.getPassenger().getPhoneNumber() : null)
                .approvedSeats(passengerRideRequest.getRequestedSeats())
                .requestStatus(rideRequestStatus.toString())
                .decisionTime(decisionTime)
                .build();
    }
    //    helper methods
//    generate the ride code
    private String generateRideCode() {
        return "RIDE-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }
//    calculate the price/km of the ride
    private BigDecimal calculatePricePerKm(
            VehicleCategory category,
            VehicleClass vehicleClass) {
        BigDecimal basePrice;
        switch (category) {
            case HATCHBACK -> basePrice = BigDecimal.valueOf(6);
            case SEDAN -> basePrice = BigDecimal.valueOf(8);
            case SUV -> basePrice = BigDecimal.valueOf(10);
            case MUV -> basePrice = BigDecimal.valueOf(9);
            case AUTO_RICKSHAW -> basePrice = BigDecimal.valueOf(5);
            case BIKE -> basePrice = BigDecimal.valueOf(4);
            default -> throw new IllegalArgumentException("Unsupported vehicle category.");
        }
        return switch (vehicleClass) {
            case ECONOMY -> basePrice;
            case STANDARD -> basePrice.add(BigDecimal.valueOf(2));
            case PREMIUM -> basePrice.add(BigDecimal.valueOf(4));
        };
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
//    validate user's account status
    private void validateUserAccount(UserEntity user) {
        if(user.getUserAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("User's account is suspended. Please try after some time.");
        if(user.getUserAccountStatus().equals(UserAccountStatus.DEACTIVATED))
            throw new AccessDeniedException("User's account is deactivated. Please try after some time.");
        if(!user.getUserRoles().contains(UserRole.DRIVER_ROLE))
            throw new AccessDeniedException("You do not have to access this resource.");
    }
}
