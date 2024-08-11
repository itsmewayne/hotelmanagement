package org.hotel.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hotel.hotel.entity.Bed;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomRequest {
    private Long id;
    private Integer roomNumber;
    private String type;
    private Double pricePerMonth;
    private Boolean isOccupied=false;
    private Integer numberOfBeds;
    private List<BedRequest> beds=new ArrayList<>();

}
