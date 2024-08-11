package org.hotel.hotel.repository;

import org.hotel.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    boolean existsByName(String name);
    List<Hotel> findByAdmin_Email(String email);

    Optional<Hotel> findByUser_Email(String email);

    Hotel findHotelByUserEmail(String email);

}
