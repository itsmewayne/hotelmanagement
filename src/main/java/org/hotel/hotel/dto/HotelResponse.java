package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {

    private Long id;
    private String name;
    private String location;
    private Float rating;
    private List<RoomResponse> rooms;
    private Integer totalRooms;

}