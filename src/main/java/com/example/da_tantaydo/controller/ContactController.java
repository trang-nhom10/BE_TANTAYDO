package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.ContactRequestDTO;
import com.example.da_tantaydo.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor

public class ContactController {

    private final ContactService contactService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_CONTACT')")
    public ResponseEntity<?> create(@RequestBody ContactRequestDTO request) {
        try {
            contactService.create(request);
            return ResponseEntity.ok("Contact created successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CONTACT')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            contactService.delete(id);
            return ResponseEntity.ok("Contact deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CONTACT')")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(contactService.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }
}
