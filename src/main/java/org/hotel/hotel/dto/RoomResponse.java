package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hotel.hotel.entity.Bed;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;
    private String roomNumber;
    private String type;
    private Double price;
    private List<BedRequest> beds;
    private Boolean isOccupied;
}
