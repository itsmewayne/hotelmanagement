package org.hotel.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hotel.hotel.dto.RoomType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    private Double price;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<Bed> beds=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private Boolean isOccupied;
    private Integer roomNumber;

}
