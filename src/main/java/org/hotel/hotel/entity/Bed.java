package org.hotel.hotel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bedNumber;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private Boolean isOccupied;

}
