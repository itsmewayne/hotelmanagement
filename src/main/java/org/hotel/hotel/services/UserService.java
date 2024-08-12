package org.hotel.hotel.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hotel.hotel.dto.HotelResponse;
import org.hotel.hotel.dto.UserRequest;
import org.hotel.hotel.dto.UserResponse;
import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.entity.Hotel;
import org.hotel.hotel.entity.User;
import org.hotel.hotel.exceptions.EmailAlreadyExistsException;
import org.hotel.hotel.exceptions.HotelAlreadyExists;
import org.hotel.hotel.exceptions.UserAlreadyAssignedException;
import org.hotel.hotel.repository.HotelRepository;
import org.hotel.hotel.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static java.lang.String.*;
import static java.lang.String.format;
import static java.rmi.server.LogStream.log;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse saveNewUser(UserRequest userRequest, Long hotelId) {
        // Check if a user with the given email already exists
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(
                    format("User with email %s already exists", userRequest.getEmail()));
        }

        // Fetch the hotel entity
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelAlreadyExists(format("Hotel with id %s not found", hotelId)));
        // Check if the hotel already has a user
        if (hotel.getUser() != null) {
            throw new UserAlreadyAssignedException(format("Hotel with id %s already has a user assigned", hotelId));
        }       // Encode the user's password
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Set roles for the user
        userRequest.setRoles(Arrays.asList("USER"));

        // Create a new User entity and map the user request to it
        User user = modelMapper.map(userRequest, User.class);

        // Associate the user with the hotel
        user.setHotel(hotel);

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Map the saved user entity to a UserResponse DTO and return it
        return UserResponse.builder()
                .hotel(modelMapper.map(user.getHotel(),HotelResponse.class))
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }


    public UserResponse findUserByEmail(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailAlreadyExistsException(format("User with email %s not found", email)));

        return UserResponse.builder()
                .email(user.getEmail())
                .hotel(modelMapper.map(user.getHotel(),HotelResponse.class))
                .username(user.getUsername())
                .id(user.getId())
                .build();

    }
    public UserResponse findById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(() -> new AccessDeniedException(format("You are not authorized ")));
        return modelMapper.map(user,UserResponse.class);
    }
}
