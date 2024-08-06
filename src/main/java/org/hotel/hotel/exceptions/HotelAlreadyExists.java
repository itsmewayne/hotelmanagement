package org.hotel.hotel.exceptions;

import lombok.Data;

@Data
public class HotelAlreadyExists extends RuntimeException{

    public HotelAlreadyExists(String message) {
        super(message);
    }
}
