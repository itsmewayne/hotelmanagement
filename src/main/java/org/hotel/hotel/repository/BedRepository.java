package org.hotel.hotel.repository;

import org.hotel.hotel.entity.Bed;
import org.hotel.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {

    @Query("SELECT b FROM Bed b WHERE b.isOccupied = false AND b.room.hotel = :hotel")
    List<Bed> findNonOccupied(@Param("hotel") Hotel hotel);

}
