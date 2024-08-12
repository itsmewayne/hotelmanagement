package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponse {

    private Long id;
    private String name;
    private String location;
    private Float rating;
    private List<RoomResponse> rooms=new ArrayList<>();
    private Integer totalRooms;

}