package org.hotel.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private Float rating;
    private LocalDateTime dateTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Room> rooms=new ArrayList<>();
    private Integer totalRooms;

    private Boolean isOccupied=false;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnore
    private AdminDetails admin;

    @OneToOne(mappedBy = "hotel")
    private User user;
}
