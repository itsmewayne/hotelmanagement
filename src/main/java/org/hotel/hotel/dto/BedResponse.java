package org.hotel.hotel.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hotel.hotel.entity.Room;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BedResponse {

    private Long id;

    private Long bedNumber;

    private Integer roomId;

    private Boolean isOccupied;

}
