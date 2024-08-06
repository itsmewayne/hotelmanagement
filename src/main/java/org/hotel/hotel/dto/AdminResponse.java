package org.hotel.hotel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminResponse {
    private String username;
    private String password;
    private String email;
}
