package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistoryRequest {

    private String reservationId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean isBedOccupied;
    private boolean isRoomOccupied;
    private boolean isHotelOccupied;
    private Long bedId;
    private Long roomId;
    private Long hotelId;
}
