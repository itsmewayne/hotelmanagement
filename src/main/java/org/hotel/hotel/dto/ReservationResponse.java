package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private String reservationId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer roomNumber;
    private Long bedNumber;
    private Double totalPrice;
}
