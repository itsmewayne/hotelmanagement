package org.hotel.hotel.controller;

import org.hotel.hotel.dto.ReservationHistoryResponse;
import org.hotel.hotel.services.ReservationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservation-history")
public class ReservationHistoryController {

    @Autowired
    private ReservationHistoryService reservationHistoryService;
        @GetMapping("/customer")
        public ResponseEntity<List<ReservationHistoryResponse>> getCustomerReservationHistory() {
            // Fetch the authenticated user's email
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Fetch the reservation history for the authenticated user
            List<ReservationHistoryResponse> reservationHistory = reservationHistoryService.getReservationHistoryByGuestName(email);

            // Return the list of reservation history records
            return ResponseEntity.ok(reservationHistory);
        }

        @GetMapping("/{reservationId}")
        public ResponseEntity<List<ReservationHistoryResponse>> getReservationHistoryByReservationId(@PathVariable String reservationId) {
            // Fetch the authenticated user's email
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Fetch the reservation history by reservation ID and ensure the authenticated user has access
            List<ReservationHistoryResponse> reservationHistory = reservationHistoryService.getReservationHistoryByReservationId(reservationId, email);

            // Return the list of reservation history records
            return ResponseEntity.ok(reservationHistory);
        }

}
