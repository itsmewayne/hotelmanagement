package org.hotel.hotel.services;

import org.hotel.hotel.dto.ReservationHistoryResponse;
import org.hotel.hotel.entity.ReservationHistory;
import org.hotel.hotel.entity.User;
import org.hotel.hotel.exceptions.ReservationException;
import org.hotel.hotel.repository.ReservationHistoryRepository;
import org.hotel.hotel.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationHistoryService {

    @Autowired
    private ReservationHistoryRepository reservationHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public List<ReservationHistoryResponse> getReservationHistoryByGuestName(String guestName) {
        // Fetch reservation history by guest name
        List<ReservationHistory> histories = reservationHistoryRepository.findByGuestName(guestName);

        if (histories.isEmpty()) {
            throw new ReservationException("No reservation history found for guest name: " + guestName);
        }

        // Convert each ReservationHistory entity to ReservationHistoryResponse
        return histories.stream()
                .map(history -> modelMapper.map(history, ReservationHistoryResponse.class))
                .collect(Collectors.toList());
    }

    public List<ReservationHistoryResponse> getReservationHistoryByReservationId(String reservationId, String email) {
        // Fetch reservation history by reservation ID
        List<ReservationHistory> histories = reservationHistoryRepository.findByReservationId(reservationId);

        if (histories.isEmpty()) {
            throw new ReservationException("No reservation history found for reservation ID: " + reservationId);
        }

        // Fetch the user associated with the provided email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ReservationException("User not found with email: " + email));

        // Check if the user is authorized to view this reservation history
        boolean isAuthorized = histories.stream()
                .allMatch(history -> history.getHotel().getUser().getId().equals(user.getId()));

        if (!isAuthorized) {
            throw new AccessDeniedException("You are not authorized to access this reservation history.");
        }

        // Convert each ReservationHistory entity to ReservationHistoryResponse
        return histories.stream()
                .map(history -> modelMapper.map(history, ReservationHistoryResponse.class))
                .collect(Collectors.toList());
    }

}
