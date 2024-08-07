package org.hotel.hotel.services;

import jakarta.transaction.Transactional;
import org.hotel.hotel.dto.HotelRequest;
import org.hotel.hotel.dto.RoomRequest;
import org.hotel.hotel.dto.RoomResponse;
import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.entity.Hotel;
import org.hotel.hotel.entity.Room;
import org.hotel.hotel.exceptions.EmailAlreadyExistsException;
import org.hotel.hotel.exceptions.HotelAlreadyExists;
import org.hotel.hotel.repository.AdminRepository;
import org.hotel.hotel.repository.HotelRepository;
import org.hotel.hotel.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AdminService adminService;

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Transactional
    public RoomResponse createRoom(HotelRequest hotelRequest, Hotel savedhotel, String email) {
        AdminDetails admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new EmailAlreadyExistsException(format("Admin  with id %s not found",email)));

        Hotel hotel = admin.getHotels().stream()
                .filter(h -> h.getId().equals(savedhotel.getId()))
                .findFirst()
                .orElseThrow(() -> new HotelAlreadyExists(format("Hotel  with id %s not found",savedhotel.getId())));

        List<Room> rooms = new ArrayList<>();
        Integer roomNumber = hotelRequest.getIntialRoomNumber();
        for (int i = 0; i < hotelRequest.getTotalRooms(); i++) {
            RoomRequest roomRequest = hotelRequest.getRooms().get(i);
            roomRequest.setRoomNumber(roomNumber + i); // Increment room number for each room
            Room newRoom = modelMapper.map(roomRequest, Room.class);
            newRoom.setHotel(hotel); // Set the hotel reference
            rooms.add(newRoom);
        }
        // Set the rooms in the hotel
        hotel.setRooms(rooms);

        return modelMapper.map(rooms, RoomResponse.class);
    }


    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        existingRoom.setRoomNumber(roomDetails.getRoomNumber());
        existingRoom.setType(roomDetails.getType());
        existingRoom.setPrice(roomDetails.getPrice());
        existingRoom.setBeds(roomDetails.getBeds());
        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }

//    public void saveRoom(Hotel hotel, String email) {
//        AdminDetails admin = adminRepository.findByEmail(email)
//                .orElseThrow(() -> new EmailAlreadyExistsException(format("Admin  with id %s not found",email)));
//
//        Hotel hotel1 = admin.getHotels().stream()
//                .filter(h -> h.getId().equals(hotel.getId()))
//                .findFirst()
//                .orElseThrow(() -> new HotelAlreadyExists(format("Hotel  with id %s not found",hotel.getId())));
//
//
//            Room newRoom = modelMapper.map(roomRequest, Room.class);
//            hotel1.getRooms().add(newRoom);
//            newRoom.setHotel(hotel); // Assuming Room has a reference back to Hotel
//            hotelRepository.save(hotel);
//
//
//    }
}
