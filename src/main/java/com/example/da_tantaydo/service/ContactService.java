package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.ContactRequestDTO;
import com.example.da_tantaydo.model.entity.Contact;
import java.util.List;

public interface ContactService {
    void create(ContactRequestDTO request);
    void delete(Long id);
    List<Contact> getAll();
}