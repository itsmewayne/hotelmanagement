package org.hotel.hotel.services;

import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.repository.AdminRepository;
import org.hotel.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if the email belongs to an admin
        Optional<AdminDetails> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            AdminDetails adminDetails = adminOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(adminDetails.getEmail())
                    .password(adminDetails.getPassword())
                    .roles(adminDetails.getRoles().toArray(new String[0]))
                    .build();
        }

        // Check if the email belongs to a regular user
        Optional<org.hotel.hotel.entity.User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            org.hotel.hotel.entity.User user = userOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }

        // If the email is not found in either repository, throw an exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

