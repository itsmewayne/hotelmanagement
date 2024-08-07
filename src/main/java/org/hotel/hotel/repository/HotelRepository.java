package org.hotel.hotel.repository;

import org.hotel.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    boolean existsByName(String name);
    List<Hotel> findByAdmin_Email(String email);
}
