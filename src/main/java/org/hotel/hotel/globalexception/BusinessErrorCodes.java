package org.hotel.hotel.globalexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    HOTEL_NOT_FOUND(305, BAD_REQUEST, "Hotel not found"),
    HOTEL_FULL(403,FORBIDDEN, "The hotel is fully occupied."),
    ROOM_EXCEPTION(305, BAD_REQUEST, "Room not found "),
    RESOURCE_NOT_FOUND(305, BAD_REQUEST, "Resource not found"),
    USER_ASSIGNED_ALREADY(305, BAD_REQUEST, "User Assigned Already"),

    EMAIL_EXISTS(305,BAD_REQUEST,"Email Already Exists"),
    ACCESS_DENIED(403,BAD_REQUEST,"You are not authorized to access this resource"),
    RESERVATION_EXCEPTION(305,BAD_REQUEST,"Reservation Already Exists"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }

}