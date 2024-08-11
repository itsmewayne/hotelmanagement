package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private Double totalPrice;
    private Long bedId;
}
