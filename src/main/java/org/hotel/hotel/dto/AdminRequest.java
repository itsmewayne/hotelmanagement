package org.hotel.hotel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AdminRequest {
    private String username;
    private String password;
    private String email;
    private List<String> roles=new ArrayList<>();
}
