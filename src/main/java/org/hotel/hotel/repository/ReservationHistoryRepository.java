package org.hotel.hotel.repository;

import org.hotel.hotel.entity.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    // Find reservation histories by guest name
    List<ReservationHistory> findByGuestName(String guestName);

    // Find reservation histories by reservation ID
    List<ReservationHistory> findByReservationId(String reservationId);
}
