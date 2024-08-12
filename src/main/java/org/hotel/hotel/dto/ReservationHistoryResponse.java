package org.hotel.hotel.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationHistoryResponse {

    private Long id;
    private String reservationId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean isBedOccupied;
    private boolean isRoomOccupied;
    private boolean isHotelOccupied;
    private String bedId;
    private String roomId;
    private String hotelName;
}
