package org.hotel.hotel.services;

import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminDetails adminDetails=adminRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: " + email));
        return User.builder()
                .username(adminDetails.getEmail())
                .password(adminDetails.getPassword())
                .roles(adminDetails.getRoles().toArray(new String[0]))
                .build();
    }
}
