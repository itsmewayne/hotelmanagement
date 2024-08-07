package org.hotel.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AdminDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String password;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "admin_roles", joinColumns = @JoinColumn(name = "admin_id"))
    private List<String> roles=new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotel> hotels = new ArrayList<>();
}
