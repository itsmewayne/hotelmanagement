package org.hotel.hotel.controller;

import lombok.extern.slf4j.Slf4j;
import org.hotel.hotel.dto.AdminRequest;
import org.hotel.hotel.dto.AdminResponse;
import org.hotel.hotel.dto.DeletedResponse;
import org.hotel.hotel.services.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<AdminResponse>> getAdmins()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());
        List<AdminResponse> adminResponseList = adminService.getAllAdmin();
        if (!adminResponseList.isEmpty())
        {
            return new ResponseEntity<>(adminResponseList,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<AdminResponse> getAdmin(@PathVariable Long id)
    {
        AdminResponse adminResponse = adminService.getById(id);
        if (adminResponse!=null)
        {
            return new ResponseEntity<>(adminResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("id/{id}")
    public ResponseEntity<DeletedResponse> deleteAdmin(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();

        boolean deletedAdmin = adminService.deleteAdmin(id, authenticatedEmail);
        if (deletedAdmin) {
            return new ResponseEntity<>(new DeletedResponse("Admin deleted successfully", true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DeletedResponse("Unauthorized to delete this admin", false), HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<AdminResponse> updateAdmin(@RequestBody AdminRequest adminRequest)
    {
        AdminResponse adminResponse = adminService.updateAdmin(adminRequest);
        if (adminResponse!=null)
        {
            return new ResponseEntity<>(adminResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
