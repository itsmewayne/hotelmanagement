package org.hotel.hotel.repository;

import org.hotel.hotel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("SELECT r FROM Reservation r WHERE r.bed.id = :bedId AND r.room.id = :roomId AND " +
            "((r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate))")
    List<Reservation> findByBedIdAndRoomIdAndDateRange(@Param("bedId") Long bedId,
                                                       @Param("roomId") Long roomId,
                                                       @Param("checkInDate") LocalDate checkInDate,
                                                       @Param("checkOutDate") LocalDate checkOutDate);

    Optional<Reservation> findById(String id);
}
