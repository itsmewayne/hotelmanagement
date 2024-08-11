package org.hotel.hotel.globalexception;

import org.hotel.hotel.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(HotelAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> customerNotFound(HotelAlreadyExists hotelAlreadyExists)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .error(hotelAlreadyExists.getMessage())
                        .ErrorCode(BusinessErrorCodes.HOTEL_NOT_FOUND.getCode())
                        .ErrorDescription(BusinessErrorCodes.HOTEL_NOT_FOUND.getDescription())
                        .build());
    }
    @ResponseBody
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> emailAlreadyExists(EmailAlreadyExistsException emailAlreadyExistsException)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .error(emailAlreadyExistsException.getMessage())
                        .ErrorCode(BusinessErrorCodes.EMAIL_EXISTS.getCode())
                        .ErrorDescription(BusinessErrorCodes.EMAIL_EXISTS.getDescription())
                        .build());
    }
    @ResponseBody
    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ExceptionResponse> roomAlreadyexits(RoomException roomException)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .ErrorDescription(BusinessErrorCodes.ROOM_EXCEPTION.getDescription())
                                .ErrorCode(BusinessErrorCodes.ROOM_EXCEPTION.getCode())
                                .error(roomException.getMessage())
                                .build()
                );
    }
    @ResponseBody
    @ExceptionHandler(UserAlreadyAssignedException.class)
    public ResponseEntity<ExceptionResponse> userAlreadyAssigned(UserAlreadyAssignedException userAlreadyAssignedException)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .ErrorCode(BusinessErrorCodes.USER_ASSIGNED_ALREADY.getCode())
                                .ErrorDescription(BusinessErrorCodes.USER_ASSIGNED_ALREADY.getDescription())
                                .error(userAlreadyAssignedException.getMessage())
                                .build()
                );
    }
    @ResponseBody
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ExceptionResponse> reservationException(ReservationException reservationException)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(reservationException.getMessage())
                                .ErrorDescription(BusinessErrorCodes.RESERVATION_EXCEPTION.getDescription())
                                .ErrorCode(BusinessErrorCodes.RESERVATION_EXCEPTION.getCode())
                                .build()
                );
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ExceptionResponse.builder()
                        .ErrorDescription(BusinessErrorCodes.ACCESS_DENIED.getDescription())
                        .error(ex.getMessage())
                        .ErrorCode(BusinessErrorCodes.ACCESS_DENIED.getCode())
                        .build());
    }
    @ExceptionHandler(HotelFullException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleHotelFullException(HotelFullException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .error(ex.getMessage())
                        .ErrorCode(BusinessErrorCodes.HOTEL_FULL.getCode())
                        .ErrorDescription(BusinessErrorCodes.HOTEL_FULL.getDescription())
                        .build());
    }
}
