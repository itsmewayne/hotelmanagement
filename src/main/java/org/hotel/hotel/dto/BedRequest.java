package org.hotel.hotel.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedRequest {

    private Integer bedNumber;
    private Boolean isOccupied=false;
}
