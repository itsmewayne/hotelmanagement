package org.hotel.hotel.controller;

import org.hotel.hotel.dto.AdminResponse;
import org.hotel.hotel.dto.DeletedResponse;
import org.hotel.hotel.dto.HotelRequest;
import org.hotel.hotel.dto.HotelResponse;
import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.entity.Hotel;
import org.hotel.hotel.services.AdminService;
import org.hotel.hotel.services.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;


    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<HotelResponse> hotels = hotelService.getAllHotels(email);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('USER')")
    @GetMapping("/all")
    public ResponseEntity<HotelResponse> getAllHotelsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        HotelResponse hotel = hotelService.getAllHotelsByUsers(email);
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        Optional<HotelResponse> hotelResponseOptional = hotelService.getHotelById(id);

        return hotelResponseOptional
                .map(hotelResponse -> new ResponseEntity<>(hotelResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@RequestBody HotelRequest hotelRequest) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        HotelResponse newHotel = hotelService.createHotel(hotelRequest,email);
        if (newHotel!=null)
        {
        return new ResponseEntity<>(newHotel, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id, @RequestBody HotelRequest hotelDetails) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        HotelResponse updatedHotel = hotelService.updateHotel(id, hotelDetails,email);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeletedResponse> deleteHotel(@PathVariable Long id) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean deleted = hotelService.deleteHotel(id, email);
        if (deleted)
        {
        return new ResponseEntity<>(new DeletedResponse("successfull deletion",true),HttpStatus.OK);
        }
        return new ResponseEntity<>(new DeletedResponse("Unsuccessfull deletion",false),HttpStatus.NOT_FOUND);
    }
}
