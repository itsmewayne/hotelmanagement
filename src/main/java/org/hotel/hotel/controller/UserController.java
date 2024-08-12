package org.hotel.hotel.controller;

import org.hotel.hotel.dto.HotelResponse;
import org.hotel.hotel.dto.UserRequest;
import org.hotel.hotel.dto.UserResponse;
import org.hotel.hotel.services.HotelService;
import org.hotel.hotel.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HotelService hotelService;


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Fetch the user details by ID
        UserResponse userResponse = userService.findById(id);

        // Check if the logged-in user is trying to access their own details
        if (userResponse != null && userResponse.getEmail().equals(email)) {
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        // Return 404 if the user is not found or the ID doesn't match the logged-in user's email
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
