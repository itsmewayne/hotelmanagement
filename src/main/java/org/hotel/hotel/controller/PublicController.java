package org.hotel.hotel.controller;

import lombok.extern.slf4j.Slf4j;

import org.hotel.hotel.dto.AdminRequest;
import org.hotel.hotel.dto.AdminResponse;
import org.hotel.hotel.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AdminService adminService;

//    @Autowired
//    private JwtUtil jwtUtil;


    @PostMapping("/signup")
    public ResponseEntity<?> createAdmin(@RequestBody AdminRequest adminRequest)
    {
        AdminResponse admin = adminService.createAdmin(adminRequest);
        if (admin!=null)
        {
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/superadmin/signup")
    public ResponseEntity<?> createSuperAdmin(@RequestBody AdminRequest adminRequest) {
        // This endpoint should be protected or available only under specific conditions
        // Add your own security or validation logic here

        AdminResponse admin = adminService.saveSuperAdmin(adminRequest);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        try{
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
//            String jwt = jwtUtil.generateToken(userDetails.getUsername());
//            return new ResponseEntity<>(jwt, HttpStatus.OK);
//        }catch (Exception e){
//            log.error("Exception occurred while createAuthenticationToken ", e);
//            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
//        }
//    }
}