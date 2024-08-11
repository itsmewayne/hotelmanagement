//package org.hotel.hotel.controller;
//
//import org.hotel.hotel.dto.AdminResponse;
//import org.hotel.hotel.dto.BedRequest;
//import org.hotel.hotel.dto.BedResponse;
//import org.hotel.hotel.entity.Bed;
//import org.hotel.hotel.services.AdminService;
//import org.hotel.hotel.services.BedService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/beds")
//public class BedController {
//
//    @Autowired
//    private BedService bedService;
//
////    @GetMapping("/unoccupied")
////    public ResponseEntity<List<BedResponse>> getAllUnoccupiedBedsWithRooms() {
////        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
////        String email = authentication.getName();
////        List<BedResponse> unoccupiedBeds = bedService.getAllUnoccupiedBedsWithRooms(email);
////        return ResponseEntity.ok(unoccupiedBeds);
////    }
////}
