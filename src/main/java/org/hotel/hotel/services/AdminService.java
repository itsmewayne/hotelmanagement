package org.hotel.hotel.services;

import org.hotel.hotel.dto.AdminRequest;
import org.hotel.hotel.dto.AdminResponse;
import org.hotel.hotel.entity.AdminDetails;
import org.hotel.hotel.exceptions.EmailAlreadyExistsException;
import org.hotel.hotel.repository.AdminRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.*;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public AdminResponse createAdmin(AdminRequest adminRequest)
    {
        if (adminRepository.existsByEmail(adminRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Admin with Email already exists");//Write an customexceptionhandler for this.....
        }
        AdminDetails admin=modelMapper.map(adminRequest,AdminDetails.class);
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        admin.setRoles(Arrays.asList("ADMIN"));
        adminRepository.save(admin);
        return modelMapper.map(admin,AdminResponse.class);
    }

    public boolean saveNewUser(AdminRequest adminRequest) {
        try {
            adminRequest.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
            adminRequest.setRoles(Arrays.asList("USER"));
            adminRepository.save(modelMapper.map(adminRequest,AdminDetails.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public AdminResponse saveSuperAdmin(AdminRequest adminRequest) {

        if (adminRepository.existsByEmail(adminRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Superadmin with Email already exists");
        }
        adminRequest.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        adminRequest.setRoles(Arrays.asList("SUPERADMIN"));
        AdminDetails saved = adminRepository.save(modelMapper.map(adminRequest, AdminDetails.class));
        return modelMapper.map(saved,AdminResponse.class);
    }

    public List<AdminResponse> getAllAdmin()
    {
        List<AdminDetails> adminDetailsList = adminRepository.findAll();
        List<AdminResponse> adminResponseList = adminDetailsList.stream().map(admin -> modelMapper.map(admin, AdminResponse.class)).collect(Collectors.toList());
        return adminResponseList;
    }

    public AdminResponse getById(Long id)
    {
            AdminDetails byEmail = adminRepository.findById(id).orElseThrow(()->new RuntimeException(format("Admin With id %s not exists",id)));
        return modelMapper.map(byEmail,AdminResponse.class);
    }


    @Transactional
    public boolean deleteAdmin(Long id,String email)
    {
        AdminDetails adminDetails = adminRepository.findById(id).orElseThrow(() ->
                new RuntimeException(format("Admin with id %s not exists", id)));
        String adminEmail = adminDetails.getEmail();
        if (email.equals(adminEmail)) {
            adminRepository.delete(adminDetails);
            return true;
        }
        AdminDetails adminDetails1 = adminRepository.findByEmail(email).orElseThrow(() -> new EmailAlreadyExistsException(format("Admin with email %s not exists", email)));
        System.out.println(adminDetails1.getRoles());
        List<String> roles = adminDetails1.getRoles();
        boolean isSuperAdmin = roles.contains("SUPERADMIN");

        if (isSuperAdmin) {
            adminRepository.delete(adminDetails);
            return true;
        }

        return false;

    }

    public AdminResponse getAdminByEmail(String email)
    {
        AdminDetails adminDetails = adminRepository.findByEmail(email).orElseThrow(() -> new EmailAlreadyExistsException(format("Admin With email %s not exists", email)));
        return modelMapper.map(adminDetails,AdminResponse.class);

    }
    public AdminResponse updateAdmin(AdminRequest adminRequest)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    AdminDetails adminByEmail = adminRepository.findByEmail(authentication.getName()).orElseThrow(() -> new EmailAlreadyExistsException(format("Admin With email %s not exists", authentication.getName())));
        adminByEmail.setEmail(adminRequest.getEmail());
        adminByEmail.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        adminByEmail.setUsername(adminRequest.getUsername());
        List<String> roles = adminByEmail.getRoles();
        System.out.println("Updated roles: " + roles);

        adminRequest.setRoles(roles);
        adminRepository.save(adminByEmail);
        return modelMapper.map(adminByEmail,AdminResponse.class);
    }
}
