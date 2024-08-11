package org.hotel.hotel.repository;

import org.hotel.hotel.entity.Hotel;
import org.hotel.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomNumberAndHotel(Integer roomNumber, Hotel hotel);

    List<Room> findByHotelId(Long hotelId);
}
