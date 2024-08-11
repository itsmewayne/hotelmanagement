package org.hotel.hotel.services;

import org.hotel.hotel.dto.AdminResponse;
import org.hotel.hotel.dto.BedRequest;
import org.hotel.hotel.dto.BedResponse;
import org.hotel.hotel.dto.UserResponse;
import org.hotel.hotel.entity.Bed;
import org.hotel.hotel.entity.Hotel;
import org.hotel.hotel.entity.User;
import org.hotel.hotel.repository.BedRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedService {

    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    public List<BedResponse> getAllUnoccupiedBedsWithRooms(String email) {
        // Find the user by email
        UserResponse userResponse = userService.findUserByEmail(email);
        User user = modelMapper.map(userResponse, User.class);

        // Get the hotel associated with this user
        Hotel hotel = user.getHotel();

        // Find all unoccupied beds in the hotel
        List<Bed> unoccupiedBeds = bedRepository.findNonOccupied(hotel);

        // Map the beds to BedResponse objects
        return unoccupiedBeds.stream()
                .map(bed -> modelMapper.map(bed, BedResponse.class))
                .collect(Collectors.toList());
    }

}
