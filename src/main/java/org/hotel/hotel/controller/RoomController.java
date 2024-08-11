//package org.hotel.hotel.controller;
//
//import org.hotel.hotel.dto.RoomRequest;
//import org.hotel.hotel.dto.RoomResponse;
//import org.hotel.hotel.entity.Room;
//import org.hotel.hotel.services.RoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/rooms")
//public class RoomController {
//
//    @Autowired
//    private RoomService roomService;
//
////    @PostMapping("/{hotelId}")
////    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest, @PathVariable Long hotelId) {
////        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
////        String email = authentication.getName();
////        RoomResponse createdRoom = roomService.createRoom(roomRequest,email,hotelId);
////        if (createdRoom!=null)
////        {
////        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
////        }
////        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
//        Optional<Room> room = roomService.getRoomById(id);
//        return room.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
//        Room updatedRoom = roomService.updateRoom(id, roomDetails);
//        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
//        roomService.deleteRoom(id);
//        return ResponseEntity.noContent().build();
//    }
//}
