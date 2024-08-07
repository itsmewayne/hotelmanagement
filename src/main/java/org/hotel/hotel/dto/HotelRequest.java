package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hotel.hotel.entity.AdminDetails;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {
    private Long id;
    private String name;
    private String location;
    private Float rating;
    private LocalDateTime localDateTime;
    private AdminResponse adminResponse;
    private List<RoomRequest> rooms;
    private Integer totalRooms;
    private Integer intialRoomNumber;

}