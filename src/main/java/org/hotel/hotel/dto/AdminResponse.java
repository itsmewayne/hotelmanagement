package org.hotel.hotel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hotel.hotel.entity.Hotel;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminResponse {
    private String username;
    private String password;
    private String email;
    private List<Hotel> hotels;
}
