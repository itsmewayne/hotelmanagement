package org.hotel.hotel.services;

import lombok.extern.slf4j.Slf4j;
import org.hotel.hotel.dto.ReservationRequest;
import org.hotel.hotel.dto.ReservationResponse;
import org.hotel.hotel.entity.*;
import org.hotel.hotel.exceptions.*;
import org.hotel.hotel.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.String.*;

@Service
@Slf4j
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ReservationHistoryRepository reservationHistoryRepository;
    public boolean checkBedAvailabilityTime(Long bedId, LocalDate checkInDate, LocalDate checkOutDate, Long roomId) {
        // Find all reservations overlapping with the provided dates for the specific bed and room
        List<Reservation> overlappingReservations = reservationRepository.findByBedIdAndRoomIdAndDateRange(bedId, roomId, checkInDate, checkOutDate);
        return overlappingReservations.isEmpty();
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest reservationRequest, String userEmail) {
        // Fetch the room entity
        Room room = roomRepository.findById(reservationRequest.getRoomId())
                .orElseThrow(() -> new RoomException(format("Room not found with id %s", reservationRequest.getRoomId())));

        // Fetch the hotel entity associated with the room
        Hotel hotel = room.getHotel(); // Assuming Room has a reference to Hotel
        // Check if the hotel is fully occupied
        if (updateHotelOccupancyStatus(hotel.getId())) {
            hotel.setIsOccupied(true);
            hotelRepository.save(hotel);
            log.info("Hotel ID: " + hotel.getId() + " is now fully occupied.");
            throw new HotelFullException("Hotel is fully occupied."); // Throw custom exception
        }
        // Fetch the bed entity
        Bed bed = bedRepository.findById(reservationRequest.getBedId())
                .orElseThrow(() -> new RoomException(format("Bed with id %s not found", reservationRequest.getBedId())));

        // Fetch the user associated with the email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserAlreadyAssignedException(format("User not found with email %s", userEmail)));

        // Check if the user is associated with the hotel
        if (!hotel.getUser().equals(user)) {
            throw new AccessDeniedException("User is not authorized to book a room in this hotel.");
        }

        // Check if the bed is available for the specified date range
        if (!checkBedAvailabilityTime(reservationRequest.getBedId(), reservationRequest.getCheckInDate(), reservationRequest.getCheckOutDate(), reservationRequest.getRoomId())) {
            throw new RoomException("The bed is not available for the selected dates.");
        }

        // Validate dates
        if (reservationRequest.getCheckInDate().isAfter(reservationRequest.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }

        // Calculate the number of days for the reservation
        long numberOfDays = ChronoUnit.DAYS.between(reservationRequest.getCheckInDate(), reservationRequest.getCheckOutDate());

        // Calculate the price per day based on the default price for one month
        // Assuming a month has an average of 30 days
        double pricePerDay = room.getPricePerMonth() / 30;

        // Calculate the total price
        Double totalPrice = numberOfDays * pricePerDay;
        log.info(format("Total price for %d days is %.2f", numberOfDays, totalPrice));

        // Create a new reservation using the builder pattern
        Reservation reservation = Reservation.builder()
                .guestName(reservationRequest.getGuestName())
                .checkInDate(reservationRequest.getCheckInDate())
                .checkOutDate(reservationRequest.getCheckOutDate())
                .totalPrice(totalPrice)
                .room(room)
                .bed(bed)
                .build();

        // Save the reservation to the database
        Reservation savedReservation = reservationRepository.save(reservation);

        // Update the bed's occupancy status
        bed.setIsOccupied(true);
        bedRepository.save(bed);

        // Update the room's occupancy status if all beds are occupied
        updateRoomOccupancyStatus(reservationRequest.getRoomId());

        updateHotelOccupancyStatus(hotel.getId());


        // Log the bed status update
        log.info("Bed ID: " + bed.getId() + " status updated to occupied: " + bed.getIsOccupied());
        // Format the total price to 2 decimal places
        String formattedTotalPrice = format("%.2f", totalPrice);
        double totalPriceFormatted = Double.parseDouble(formattedTotalPrice);
        // Return the ReservationResponse with room number and bed number
        return ReservationResponse.builder()
                .reservationId(savedReservation.getId())
                .guestName(savedReservation.getGuestName())
                .checkInDate(savedReservation.getCheckInDate())
                .checkOutDate(savedReservation.getCheckOutDate())
                .roomNumber(room.getRoomNumber())
                .bedNumber(bed.getBedNumber())
                .totalPrice(totalPriceFormatted)
                .build();
    }

    @Transactional
    public void updateRoomOccupancyStatus(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(format("Room not found with id %s", roomId)));

        // Check if all beds in the room are occupied
        boolean allBedsOccupied = room.getBeds().stream()
                .allMatch(Bed::getIsOccupied);

        // Update the room's occupancy status
        room.setIsOccupied(allBedsOccupied);
        roomRepository.save(room);

        // Log the room status update
        log.info("Room ID: " + roomId + " status updated to occupied: " + room.getIsOccupied());
    }

    @Transactional
    public ReservationResponse extendCheckoutDate(String reservationId, LocalDate newCheckoutDate) {
        // Fetch the existing reservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found with id " + reservationId));

        // Fetch the room entity to get the monthly price
        Room room = reservation.getRoom();
        Double monthlyPrice = room.getPricePerMonth(); // Assuming Room entity has a getMonthlyPrice() method

        // Calculate the daily rate based on the monthly price
        int daysInMonth = 30; // Average number of days in a month
        Double dailyRate = monthlyPrice / daysInMonth;
//
//        // Check if the bed is available for the new date range
//        if (!checkBedAvailabilityTime(reservation.getBed().getId(), reservation.getCheckInDate(), newCheckoutDate, reservation.getRoom().getId())) {
//            throw new RoomException("The bed is not available for the new checkout date.");
//        }

        // Calculate the additional days
        long additionalDays = ChronoUnit.DAYS.between(reservation.getCheckOutDate(), newCheckoutDate);

        // Calculate the updated total price
        Double currentPrice = reservation.getTotalPrice(); // Assuming Reservation entity has a getPrice() method
        Double additionalPrice = additionalDays * dailyRate;
        Double updatedPrice = currentPrice + additionalPrice;

        // Update the checkout date and price
        reservation.setCheckOutDate(newCheckoutDate);
        reservation.setTotalPrice(updatedPrice);

        // Save the updated reservation
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Log the updated checkout date and price
        log.info("Reservation ID: " + reservationId + " checkout date extended to: " + newCheckoutDate + " with updated price: " + updatedPrice);

        // Return the updated reservation response
        return modelMapper.map(updatedReservation, ReservationResponse.class);
    }
    @Transactional
    public void vacateRoom(String reservationId) {
        // Fetch the reservation to be canceled
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found with id " + reservationId));

        // Get the associated bed, room, and hotel
        Bed bed = reservation.getBed();
        Room room = reservation.getRoom();
        Hotel hotel = room.getHotel();

        // Set the bed as not occupied
        bed.setIsOccupied(false);
        bedRepository.save(bed);

        // Update the room's occupancy status if there are any unoccupied beds
        updateRoomOccupancyStatus(room.getId());

        // Update the hotel's occupancy status if there are any unoccupied rooms
        updateHotelOccupancyStatus(hotel.getId());

        // Remove the reservation
        reservationRepository.delete(reservation);

        // Create or update reservation history using the builder pattern
        ReservationHistory reservationHistory = ReservationHistory.builder()
                .reservationId(reservation.getId())
                .guestName(reservation.getGuestName())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .isBedOccupied(false)
                .isRoomOccupied(room.getIsOccupied())
                .isHotelOccupied(hotel.getIsOccupied())
                .bed(bed)
                .room(room)
                .hotel(hotel)
                .build();

        reservationHistoryRepository.save(reservationHistory);

        // Log the changes
        log.info("Reservation ID: " + reservationId + " has been vacated and history updated.");
    }


    @Transactional
    public boolean updateHotelOccupancyStatus(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RoomException(format("Hotel not found with id %s", hotelId)));

        // Fetch all rooms in the hotel
        List<Room> rooms = roomRepository.findByHotelId(hotelId);

        // Check if all rooms are occupied
        boolean allRoomsOccupied = rooms.stream()
                .allMatch(Room::getIsOccupied);

        // Update the hotel's occupancy status
        hotel.setIsOccupied(allRoomsOccupied);
        hotelRepository.save(hotel);

        // Log the hotel status update
        log.info("Hotel ID: " + hotelId + " occupancy status updated to: " + hotel.getIsOccupied());
        return allRoomsOccupied;
    }



}
