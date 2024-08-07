package org.hotel.hotel.services;

import lombok.extern.slf4j.Slf4j;
import org.hotel.hotel.dto.*;
import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.entity.Bed;
import org.hotel.hotel.entity.Hotel;
import org.hotel.hotel.entity.Room;
import org.hotel.hotel.exceptions.EmailAlreadyExistsException;
import org.hotel.hotel.exceptions.HotelAlreadyExists;
import org.hotel.hotel.exceptions.RoomException;
import org.hotel.hotel.repository.AdminRepository;
import org.hotel.hotel.repository.BedRepository;
import org.hotel.hotel.repository.HotelRepository;
import org.hotel.hotel.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.*;

@Service
@Slf4j
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BedRepository bedRepository;
    public List<HotelResponse> getAllHotels(String email) {
        AdminResponse admin = adminService.getAdminByEmail(email);
        List<Hotel> byAdminEmail = hotelRepository.findByAdmin_Email(email);
         return byAdminEmail.stream()
                .map(hotel -> modelMapper.map(hotel, HotelResponse.class))
                .collect(Collectors.toList());
    }

    public Optional<HotelResponse> getHotelById(Long id) {
        // Find the hotel by its ID
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);

        // Convert to HotelResponse if present, otherwise return an empty Optional
        return hotelOptional.map(hotel -> modelMapper.map(hotel, HotelResponse.class));
    }

    @Transactional
    public HotelResponse createHotel(HotelRequest hotelRequest, String email) {
        if (hotelRepository.existsByName(hotelRequest.getName())) {
            throw new HotelAlreadyExists(format("Hotel with name %s already exists", hotelRequest.getName()));
        }

        // Retrieve the admin entity by email
        AdminDetails admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new EmailAlreadyExistsException(format("Admin with email %s not found", email)));

        // Map HotelRequest to Hotel entity
        Hotel hotel = modelMapper.map(hotelRequest, Hotel.class);
        hotel.setDateTime(LocalDateTime.now());
        hotel.setAdmin(admin);  // Set the admin for the hotel
        // Initialize the list of rooms
        hotel.setRooms(new ArrayList<>());
        // Save the hotel first to ensure it has an ID
        Hotel savedHotel = hotelRepository.save(hotel);

        // Get the initial room number from the request
        Integer roomNumber = hotelRequest.getIntialRoomNumber();

        // Track if any room number already exists
        boolean roomNumberExists = false;

        // Create and save rooms
        for (int i = 0; i < hotelRequest.getTotalRooms(); i++) {
            RoomRequest roomRequest = hotelRequest.getRooms().get(i);

            // Check if room number is null or not and set it if needed
            if (roomRequest.getRoomNumber() == null || !roomRequest.getRoomNumber().equals(roomNumber + i)) {
                roomRequest.setRoomNumber(roomNumber + i); // Increment room number for each room
            }

            Room newRoom = modelMapper.map(roomRequest, Room.class);
            newRoom.setHotel(savedHotel); // Set the hotel reference

            // Check if the room number already exists in the database
            if (roomRepository.existsByRoomNumberAndHotel(newRoom.getRoomNumber(), savedHotel)) {
                roomNumberExists = true;
                break; // Break the loop if a duplicate room number is found
            }
            List<Bed> beds=new ArrayList<>();

            for (int j = 0; j < roomRequest.getNumberOfBeds(); j++) {
                BedRequest bedRequest = BedRequest.builder()
                        .bedNumber(j + 1)
                        .isOccupied(false)
                        .build();
                Bed bed = modelMapper.map(bedRequest, Bed.class);
                bed.setRoom(newRoom);
                beds.add(bed);
            }
            bedRepository.saveAll(beds);
            newRoom.setBeds(beds);
            // Save room using RoomRepository
            roomRepository.save(newRoom);
            // Add room to the hotel's room list
            savedHotel.getRooms().add(newRoom);
        }

        // Handle room number duplication case
        if (roomNumberExists) {
            throw new RoomException(String.format("Room with room number %s already exists", roomNumber));
        }

        // Save the updated hotel with rooms
        savedHotel = hotelRepository.save(savedHotel);

        // Optionally update the admin if needed
        admin.getHotels().add(savedHotel);
        adminRepository.save(admin);

        // Return the saved hotel as a response
        return modelMapper.map(savedHotel, HotelResponse.class);

    }

    @Transactional
    public HotelResponse updateHotel(Long id, HotelRequest hotelDetails, String email) {
        // Retrieve the admin and their hotels
        AdminResponse admin = adminService.getAdminByEmail(email);
        List<Hotel> hotels = admin.getHotels();

        // Check if the hotel to be updated is associated with the admin
        if (hotels.stream().noneMatch(hotel -> hotel.getId().equals(id))) {
            throw new HotelAlreadyExists("Unauthorized to update this hotel");
        }

        // Find the hotel by ID
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelAlreadyExists("Hotel not found with ID: " + id));

        // Update hotel details
        existingHotel.setName(hotelDetails.getName());
        existingHotel.setLocation(hotelDetails.getLocation());
        existingHotel.setRating(hotelDetails.getRating());

        // Get the list of rooms to update from the request
        List<RoomRequest> roomRequestsToUpdate = hotelDetails.getRooms();

        // Convert existing rooms into a map for easy lookup
        Map<Integer, Room> existingRoomsMap = existingHotel.getRooms().stream()
                .collect(Collectors.toMap(Room::getRoomNumber, room -> room));

        // Update existing rooms based on the request
        for (RoomRequest roomRequest : roomRequestsToUpdate) {
            Integer roomNumber = roomRequest.getRoomNumber();
            Room existingRoom = existingRoomsMap.get(roomNumber);
            if (existingRoom != null) {
                // Update existing room details
                existingRoom.setType(roomRequest.getType());
                existingRoom.setPrice(roomRequest.getPrice());
                existingRoom.setIsOccupied(roomRequest.getIsOccupied());
//                existingRoom.setBeds(roomRequest.getBeds());
                existingRoom.setType(roomRequest.getType());
                // No need to update hotel reference again
            }
            else {
                throw new RoomException(format("Room with roomnumber %s not exists",roomNumber));
            }
            // If the room does not exist, do nothing (room is not added)
        }

        // Save the updated hotel
        Hotel savedHotel = hotelRepository.save(existingHotel);

        // Return the updated hotel as a response
        return modelMapper.map(savedHotel, HotelResponse.class);
    }


    @Transactional
    public boolean deleteHotel(Long id, String email) {
        // Retrieve the admin by email
        AdminDetails admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new EmailAlreadyExistsException(format("Admin with id %s not found",email)));

        // Find the hotel by ID
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelAlreadyExists(format("Hotel with id %s not found",id)));

        // Check if the hotel belongs to the admin
        if (!admin.getHotels().contains(hotel)) {
            throw new HotelAlreadyExists("Hotel does not belong to the current admin");
        }

        // Remove the hotel from the admin's list of hotels
        admin.getHotels().remove(hotel);

        // Delete the hotel from the repository
        hotelRepository.delete(hotel);

        // Optionally, save the updated admin entity if needed
        adminRepository.save(admin);
        return true;
    }

}
