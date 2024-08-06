package org.hotel.hotel.globalexception;

import org.hotel.hotel.exceptions.EmailAlreadyExistsException;
import org.hotel.hotel.exceptions.HotelAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
                        .ErrorCode(BusinessErrorCodes.CUSTOMER_NOT_FOUND.getCode())
                        .ErrorDescription(BusinessErrorCodes.CUSTOMER_NOT_FOUND.getDescription())
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
}
