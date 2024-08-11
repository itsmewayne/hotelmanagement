package org.hotel.hotel.controller;

import org.hotel.hotel.dto.BedResponse;
import org.hotel.hotel.dto.ReservationRequest;
import org.hotel.hotel.dto.ReservationResponse;
import org.hotel.hotel.exceptions.ReservationException;
import org.hotel.hotel.services.BedService;
import org.hotel.hotel.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private BedService bedService;
    @GetMapping("/check-availability")
    public ResponseEntity<?> checkBedAvailability(
            @RequestParam Long bedId,
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
    @RequestParam Long roomId) {

            boolean isAvailable = reservationService.checkBedAvailabilityTime(bedId, checkInDate, checkOutDate,roomId);
            if (isAvailable)
            {
                return new ResponseEntity<>("Beds Are Available", HttpStatus.OK);
            }
            return new ResponseEntity<>("Beds Are Not Available", HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')" )
    @PostMapping("/booking")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {
        // Get the currently authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Pass the email to the reservation service (if needed)
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest, email);
        return ResponseEntity.ok(reservationResponse);
    }

    @GetMapping("/available")
    public List<BedResponse> checkNotOccupied()
    {
        //add authentication for user
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<BedResponse> allUnoccupiedBedsWithRooms = bedService.getAllUnoccupiedBedsWithRooms(email);
        return allUnoccupiedBedsWithRooms;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')" )
    @PutMapping("/{reservationId}/extend-checkout")
    public ResponseEntity<ReservationResponse> extendCheckoutDate(
            @PathVariable String reservationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newCheckoutDate) {

        // Call the service method to extend the checkout date
        ReservationResponse updatedReservation = reservationService.extendCheckoutDate(reservationId, newCheckoutDate);

        // Return the updated reservation response
        return ResponseEntity.ok(updatedReservation);
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')" )
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> vacateRoom(@PathVariable String reservationId) {
        try {
            reservationService.vacateRoom(reservationId);
            return ResponseEntity.ok("Reservation vacated successfully.");
        } catch (ReservationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while vacating the room.");
        }
    }

}
